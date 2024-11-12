package top.xyquan.pocketorderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.xyquan.pocketorderservice.dto.UserDTO;
import top.xyquan.pocketorderservice.entity.User;
import top.xyquan.pocketorderservice.mapper.UserMapper;
import top.xyquan.pocketorderservice.service.UserService;
import top.xyquan.pocketorderservice.utils.JwtUtil;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 密码加密
    @Autowired
    private JwtUtil jwtUtil; // JWT工具类

    // 通过用户名查找用户
    @Override
    @Cacheable(value = "userCache", key = "#username")
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); // 使用字段名进行查询
        return userMapper.selectOne(queryWrapper); // 查询符合条件的单个用户
    }

    // 通过token查找用户
    @Override
    @Cacheable(value = "userCache", key = "#token")
    public User getUserByToken(String token) {
        return userMapper.getUserByToken(token);
    }

    // 用户注册
    @Override
    public UserDTO register(User user) {
        // 检查用户名是否已存在
        User existingUser = findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在"); // 可以自定义异常
        }

        // 创建新用户并设置信息
        User newUser = new User();
        newUser.setUsername(user.getUsername());

        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(encodedPassword);

        // 生成 token
        String token = jwtUtil.generateToken(newUser.getUsername());
        newUser.setToken(token); // 将 token 保存到用户对象中
        System.out.println(newUser);

        // 保存用户到数据库
        userMapper.insert(newUser);
        User newUserDb = findByUsername(user.getUsername());

        // 创建并返回 UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(newUserDb.getId());
        userDTO.setUsername(newUserDb.getUsername());
        userDTO.setAvatar(newUserDb.getAvatar());
        userDTO.setIntegrate(newUserDb.getIntegrate());
        userDTO.setRole(newUserDb.getRole());
        userDTO.setToken(newUserDb.getToken()); // 返回生成的 token

        return userDTO;
    }

    // 用户登录
    @Override
    public UserDTO login(User user) {
        // 根据用户名查询用户
        User existingUser = findByUsername(user.getUsername());

        // 用户不存在，抛出异常
        if (existingUser == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成 JWT token
        String token = jwtUtil.generateToken(existingUser.getUsername());

        // 更新用户的 token
        existingUser.setToken(token);
        userMapper.updateById(existingUser); // 更新数据库中的 token

        // 创建并返回 UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(existingUser.getId());
        userDTO.setUsername(existingUser.getUsername());
        userDTO.setAvatar(existingUser.getAvatar());
        userDTO.setIntegrate(existingUser.getIntegrate());
        userDTO.setRole(existingUser.getRole());
        userDTO.setToken(token); // 返回生成的 token

        return userDTO;
    }

}


