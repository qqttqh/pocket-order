package top.xyquan.pocketorderservice.service;


import org.springframework.data.domain.Page;
import top.xyquan.pocketorderservice.dto.OrderDTO;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);

    // 更新订单的 order_list
    void updateOrderList(OrderDTO orderDTO);

    // 更新订单的 status 为 1（成功）
    void markOrderAsSuccess(Integer orderId);

    Page<OrderDTO> getOrders(int page);

    // 取消用户订单
    void cancelOrder(Integer orderId);

    // 通过 token 或 uuid 获取订单
    Page<OrderDTO> getOrders(int page, String token, String uuid);
}
