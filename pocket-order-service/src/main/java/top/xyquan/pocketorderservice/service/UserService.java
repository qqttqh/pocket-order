package top.xyquan.pocketorderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.xyquan.pocketorderservice.dto.UserDTO;
import top.xyquan.pocketorderservice.entity.User;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    User getUserByToken(String token);

    UserDTO register(User user);
    UserDTO login(User user);

}
