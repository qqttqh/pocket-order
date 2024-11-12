package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.xyquan.pocketorderservice.dto.OrderDTO;
import top.xyquan.pocketorderservice.service.OrderService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    // 通用的响应生成方法
    private ResponseEntity<Map<String, Object>> buildResponse(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        if (data != null) {
            response.put("data", data);
        }
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(
            @RequestParam("page") int page,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "uuid", required = false) String uuid) {

        // 检查 token 和 uuid
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7); // 移除 "Bearer " 前缀
        }

        if (token == null && uuid == null) {
            return ResponseEntity.badRequest().build(); // 如果没有 token 和 uuid，返回 400 错误
        }

        // 调用 Service 获取订单
        Page<OrderDTO> orders = orderService.getOrders(page, token, uuid);

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderDTO orderDTO) {
        // 直接调用服务层的方法，抛出异常由全局异常处理器处理
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return buildResponse(createdOrder, "下单成功");
    }
}
