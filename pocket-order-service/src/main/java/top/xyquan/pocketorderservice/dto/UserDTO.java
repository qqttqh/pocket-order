package top.xyquan.pocketorderservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;       // id
    private String username;  // 用户名
    private String avatar;    // 用户头像
    private Integer integrate;// 积分
    private String role;      // 用户角色
    private String token;     // 用户 token
}
