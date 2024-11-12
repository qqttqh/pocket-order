package top.xyquan.pocketorderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu_group")
public class MenuGroup {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

}
