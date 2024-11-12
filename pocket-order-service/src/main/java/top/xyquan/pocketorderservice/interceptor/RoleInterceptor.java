package top.xyquan.pocketorderservice.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.xyquan.pocketorderservice.entity.User;
import top.xyquan.pocketorderservice.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper; // 用于生成 JSON 响应

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉 "Bearer " 前缀
        }

        User user = userService.getUserByToken(token);

        // 验证失败时返回 JSON 响应
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "非管理员");

            // 将 JSON 数据写入响应
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return false;
        }

        return true;
    }
}
