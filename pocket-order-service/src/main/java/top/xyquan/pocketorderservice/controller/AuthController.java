package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xyquan.pocketorderservice.dto.UserDTO;
import top.xyquan.pocketorderservice.entity.User;
import top.xyquan.pocketorderservice.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        UserDTO userDTO = userService.register(user); // 调用注册方法
        return ResponseEntity.ok(userDTO); // 返回 UserDTO
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) {
        UserDTO userDTO = userService.login(user); // 调用登录方法
        return ResponseEntity.ok(userDTO); // 返回 UserDTO
    }
}
