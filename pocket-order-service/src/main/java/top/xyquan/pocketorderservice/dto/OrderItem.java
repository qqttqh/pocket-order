package top.xyquan.pocketorderservice.dto;

import lombok.Data;

@Data
public class OrderItem {
    private int id;
    private String name;
    private float price;
    private int count;
    private int success;
}
