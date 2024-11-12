package top.xyquan.pocketorderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu_data")
public class MenuData {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String introduce;
    private float price;
    private String img;
    private Integer groupId;
}
