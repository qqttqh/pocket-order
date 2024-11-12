package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.xyquan.pocketorderservice.dto.OrderDTO;
import top.xyquan.pocketorderservice.entity.DailyOrderStats;
import top.xyquan.pocketorderservice.entity.MenuData;
import top.xyquan.pocketorderservice.entity.MenuGroup;
import top.xyquan.pocketorderservice.service.DailyOrderStatsService;
import top.xyquan.pocketorderservice.service.MenuDataService;
import top.xyquan.pocketorderservice.service.MenuGroupService;
import top.xyquan.pocketorderservice.service.OrderService;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MenuDataService menuDataService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DailyOrderStatsService dailyOrderStatsService;

    // 通用的响应生成方法
    private ResponseEntity<Map<String, Object>> buildResponse(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        if (data != null) {
            response.put("data", data);
        }
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    // 更新菜品信息
    @PostMapping("/menu/update")
    public ResponseEntity<Map<String, Object>> updateMenuData(@RequestBody MenuData menuData) {
        menuDataService.updateById(menuData);
        return buildResponse(null, "保存成功");
    }

    // 新增菜品
    @PostMapping("/menu/add")
    public ResponseEntity<Map<String, Object>> addMenuData(@RequestBody MenuData menuData) {
        boolean isSaved = menuDataService.addMenuData(menuData);
        return buildResponse(isSaved ? menuData : null, "添加成功");
    }

    // 删除菜品
    @PostMapping("/menu/remove")
    public ResponseEntity<Map<String, Object>> removeMenuData(@RequestBody MenuData menuData) {
        menuDataService.removeById(menuData.getId());
        return buildResponse(null, "删除成功");
    }

    // 更新分组
    @PostMapping("/menu-group/update")
    public ResponseEntity<Map<String, Object>> updateMenuGroup(@RequestBody MenuGroup menuGroup) {
        menuGroupService.updateById(menuGroup);
        return buildResponse(null, "保存成功");
    }

    // 添加分组
    @PostMapping("/menu-group/add")
    public ResponseEntity<Map<String, Object>> addMenuGroup(@RequestBody MenuGroup menuGroup) {
        boolean isSaved = menuGroupService.save(menuGroup);
        return buildResponse(isSaved ? menuGroup : null, "添加成功");
    }

    // 删除分组
    @PostMapping("/menu-group/remove")
    public ResponseEntity<Map<String, Object>> removeMenuGroup(@RequestBody MenuGroup menuGroup) {
        menuGroupService.removeGroupById(menuGroup.getId());
        return buildResponse(null, "删除成功");
    }

    // 获取订单
    @GetMapping("/orders")
    public Page<OrderDTO> getOrders(@RequestParam(defaultValue = "1") int page) {
        return orderService.getOrders(page);
    }

    // 更新订单的 order_list
    @PutMapping("/orders/update-order-list")
    public ResponseEntity<String> updateOrderList(@RequestBody OrderDTO orderDTO) {
        // 调用 Service 更新订单
        orderService.updateOrderList(orderDTO);
        return ResponseEntity.ok("订单更新成功");
    }

    // 完成订单
    @GetMapping("/orders/mark-order-as-success")
    public ResponseEntity<String> markOrderAsSuccess(@RequestParam Integer orderId) {
        // 调用 Service 将订单的状态更新为成功
        orderService.markOrderAsSuccess(orderId);
        return ResponseEntity.ok("订单状态更新为成功");
    }

    // 获取统计数据
    @GetMapping("/daily-order-stats")
    public ResponseEntity<Map<String, Object>> getLast30DaysStats() {
        Map<String, Object> response = new HashMap<>();
        // 调用 Service 层获取数据
        List<DailyOrderStats> dailyOrderStats = dailyOrderStatsService.getLast30DaysStats();
        Collections.reverse(dailyOrderStats);
        response.put("data", dailyOrderStats);
        return ResponseEntity.ok(response);
    }

    // 取消用户订单
    @DeleteMapping("/cancel-order")
    public ResponseEntity<String> cancelOrder(@RequestParam Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("订单已取消");
    }
}

