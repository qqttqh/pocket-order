package top.xyquan.pocketorderservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id; // 订单id
    private Integer user_id; // 用户id
    private String user_uuid; // 用户uuid
    private String table_number; // 桌子编号
    private List<OrderItem> order_list; // 菜品列表
    float total_price; // 总计价格
    private Integer status; // 订单状态
    private LocalDateTime create_time; // 下单时间
}
