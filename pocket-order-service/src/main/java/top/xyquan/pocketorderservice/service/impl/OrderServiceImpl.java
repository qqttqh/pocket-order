package top.xyquan.pocketorderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.xyquan.pocketorderservice.dto.OrderDTO;
import top.xyquan.pocketorderservice.dto.OrderItem;
import top.xyquan.pocketorderservice.entity.Order;
import top.xyquan.pocketorderservice.entity.User;
import top.xyquan.pocketorderservice.repository.OrderRepository;
import top.xyquan.pocketorderservice.service.OrderService;
import top.xyquan.pocketorderservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper; // 用于序列化和反序列化
    @PersistenceContext
    private EntityManager entityManager;


    // 创建订单
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // 创建 Orders 实体
        Order order = new Order();

        // 设置基本信息
        order.setUser_id(orderDTO.getUser_id());
        order.setUser_uuid(orderDTO.getUser_uuid());
        order.setTable_number(orderDTO.getTable_number());
        order.setTotal_price(orderDTO.getTotal_price());
        order.setStatus(0); // 设置初始状态
//        order.setCreate_time(LocalDateTime.now()); // 设置当前时间为 create_time

        // 将 order_list 转换为 JSON 字符串
        try {
            String orderListJson = objectMapper.writeValueAsString(orderDTO.getOrder_list());
            order.setOrder_list(orderListJson); // 将 JSON 存储到 order_list 字段
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }

        // 保存订单
        Order savedOrder = orderRepository.save(order);
        // 刷新实体以获取数据库自动生成的时间字段
        entityManager.refresh(savedOrder);

        // 反序列化 order_list 回 OrderItem 列表
        try {
            List<OrderItem> orderItems = objectMapper.readValue(savedOrder.getOrder_list(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
            // 设置反序列化后的值
            orderDTO.setId(savedOrder.getId());
            orderDTO.setCreate_time(savedOrder.getCreate_time());
            System.out.println(savedOrder.getCreate_time());
            orderDTO.setOrder_list(orderItems); // 将反序列化的菜品列表设置到 DTO
            orderDTO.setStatus(savedOrder.getStatus());

            // 如果 user_id 存在，更新用户的积分
            if (savedOrder.getUser_id() != null) {
                User user = userService.getById(savedOrder.getUser_id());
                // 更新用户的积分
                user.setIntegrate(user.getIntegrate() + (int) savedOrder.getTotal_price());
                userService.updateById(user); // 使用 MyBatis Plus 更新用户信息
            }

            return orderDTO; // 返回 OrderDTO
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    // 更新订单的 order_list
    @Override
    public void updateOrderList(OrderDTO orderDTO) {
        // 根据订单 ID 查询订单
        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("订单未找到"));

        // 将 OrderDTO 的 order_list 序列化为 JSON 字符串
        try {
            String orderListJson = objectMapper.writeValueAsString(orderDTO.getOrder_list());
            order.setOrder_list(orderListJson); // 更新订单的 order_list
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }

        // 保存更新后的订单
        orderRepository.save(order);
    }

    // 更新订单的 status 为 1（完成）
    @Override
    public void markOrderAsSuccess(Integer orderId) {
        // 根据订单 ID 查询订单
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单未找到"));

        // 更新订单的 status 为 1（成功）
        order.setStatus(1);

        // 保存更新后的订单
        orderRepository.save(order);
    }

    // 管理员获取订单
    public Page<OrderDTO> getOrders(int page) {

        // 设置分页信息，默认每页10条记录
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        // 使用 SQL 查询获取订单
        Page<Order> ordersPage = orderRepository.findPagedOrders(pageRequest);

        // 转换为 OrderDTO
        return ordersPage.map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setUser_id(order.getUser_id());
            dto.setUser_uuid(order.getUser_uuid());
            dto.setTable_number(order.getTable_number());
            dto.setTotal_price(order.getTotal_price());
            dto.setStatus(order.getStatus());
            dto.setCreate_time(order.getCreate_time());

            // 反序列化 order_list 回 OrderItem 列表
            try {
                List<OrderItem> orderItems = objectMapper.readValue(order.getOrder_list(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
                dto.setOrder_list(orderItems); // 设置菜品列表
            } catch (JsonProcessingException e) {
                throw new RuntimeException("反序列化失败", e);
            }

            return dto;
        });
    }

    // 取消用户订单
    @Override
    public void cancelOrder(Integer orderId) {
        // 查找订单
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单未找到"));

        // 如果订单有 user_id，更新用户积分
        if (order.getUser_id() != null) {
            User user = userService.getById(order.getUser_id());
            // 更新用户积分
            user.setIntegrate(user.getIntegrate() - (int) order.getTotal_price());
            userService.updateById(user); // 更新用户信息
        }

        // 删除订单
        orderRepository.deleteById(orderId);
    }

    // 通过 token 或 uuid 获取订单
    @Override
    public Page<OrderDTO> getOrders(int page, String token, String uuid) {

        // 每页 10 条记录，计算偏移量
        int pageSize = 10;
        int offset = (page - 1) * pageSize;

        // 用户通过 token 查询 userId
        Integer userId = null;
        if (token != null) {
            User user = userService.getUserByToken(token);
            if (user != null) {
                userId = user.getId();
            }
        }

        // 查询订单
        List<Order> orders;
        if (userId != null) {
            // 使用 userId 查询订单
            orders = orderRepository.findOrdersByUserId(userId, pageSize, offset);
        } else {
            // 使用 uuid 查询订单
            orders = orderRepository.findOrdersByUserUuid(uuid, pageSize, offset);
        }

        // 转换为 OrderDTO
        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setUser_id(order.getUser_id());
            dto.setUser_uuid(order.getUser_uuid());
            dto.setTable_number(order.getTable_number());
            dto.setTotal_price(order.getTotal_price());
            dto.setStatus(order.getStatus());
            dto.setCreate_time(order.getCreate_time());

            // 反序列化 order_list 回 OrderItem 列表
            try {
                List<OrderItem> orderItems = objectMapper.readValue(order.getOrder_list(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
                dto.setOrder_list(orderItems);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("反序列化失败", e);
            }

            return dto;
        }).collect(Collectors.toList());

        // 构建分页对象
        long total = orders.size(); // 你可能需要通过数据库查询总条数
        return new PageImpl<>(orderDTOs, PageRequest.of(page - 1, pageSize), total);
    }
}
