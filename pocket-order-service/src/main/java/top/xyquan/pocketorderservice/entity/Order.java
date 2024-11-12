package top.xyquan.pocketorderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer user_id;
    private String user_uuid;
    private String table_number;
    private String order_list;
    private float total_price;
    private Integer status;
    @Column(name = "create_time", insertable = false, updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime create_time;
}
