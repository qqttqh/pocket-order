package top.xyquan.pocketorderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import top.xyquan.pocketorderservice.websocket.OrdersWebSocketHandler;
import top.xyquan.pocketorderservice.websocket.TableWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TableWebSocketHandler(), "/ws/table")
                .addHandler(new OrdersWebSocketHandler(), "/ws/orders")
                .setAllowedOrigins("*"); // 允许跨域
    }
}

