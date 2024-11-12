package top.xyquan.pocketorderservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrdersWebSocketHandler extends TextWebSocketHandler {

    // 用于存储所有用户的 WebSocket 会话
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> adminSessions = new ConcurrentHashMap<>(); // 存储管理员会话
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 获取 userId 和 role 参数
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            String[] userIdHolder = new String[1]; // 用数组来存储 userId
            String[] roleHolder = new String[1]; // 用数组来存储 role

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if ("userId".equals(keyValue[0])) {
                            userIdHolder[0] = keyValue[1]; // 获取 userId 参数
                        } else if ("role".equals(keyValue[0])) {
                            roleHolder[0] = keyValue[1]; // 获取 role 参数
                        }
                    }
                }
            }

            // 确保 userId 提供了
            if (userIdHolder[0] != null) {
                session.getAttributes().put("userId", userIdHolder[0]); // 将 userId 存入 session 属性
                userSessions.put(userIdHolder[0], session); // 将用户加入队列

                // 如果角色为 ADMIN，将其添加到管理员队列
                if ("ADMIN".equals(roleHolder[0])) {
                    session.getAttributes().put("role", "ADMIN");
                    adminSessions.put(userIdHolder[0], session);
                }

//                System.out.println("用户 " + userIdHolder[0] + " 已连接，角色: " + roleHolder[0]);
            } else {
                session.close(); // 如果未提供 userId，关闭连接
//                System.err.println("连接时未提供 userId 参数，关闭连接");
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Map<String, Object> payload = parseMessage(message.getPayload());
            String type = (String) payload.get("type");

            switch (type) {
                case "update-order-progress":  // 更新订单进度
                case "mark-order-as-success": // 标记订单完成
                case "cancel-order": // 取消用户订单
                    handleUpdateOrderData(payload, session); // 处理订单数据更新
                    break;
                case "create-order":
                    handleCreateOrder(payload, session); // 处理创建订单
                    break;
                default:
                    System.out.println("未知消息类型: " + type);
            }
        } catch (IOException e) {
            System.err.println("处理消息时发生IO异常: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("处理消息时发生异常: " + e.getMessage());
        }
    }

    // 处理创建订单
    private void handleCreateOrder(Map<String, Object> payload, WebSocketSession session) throws IOException {
        // 转发给所有管理员
        for (WebSocketSession adminSession : adminSessions.values()) {
            if (adminSession.isOpen()) {
                sendMessage(adminSession, payload); // 发送创建订单消息给管理员
//                System.out.println("转发创建订单数据给管理员 " + adminSession.getId());
            }
        }
    }

    // 处理更新订单数据
    private void handleUpdateOrderData(Map<String, Object> payload, WebSocketSession session) throws IOException {
        Object targetIdObj = payload.get("targetId");
        String targetId;

        // 检查 targetId 的类型并进行转换
        if (targetIdObj instanceof String) {
            targetId = (String) targetIdObj;
        } else if (targetIdObj instanceof Integer) {
            targetId = String.valueOf(targetIdObj); // 将 Integer 转换为 String
        } else {
            System.err.println("目标ID类型不支持: " + targetIdObj.getClass().getName());
            return;
        }

        // 查找目标用户的会话并发送消息
        WebSocketSession targetSession = userSessions.get(targetId);
        if (targetSession != null && targetSession.isOpen()) {
            sendMessage(targetSession, payload); // 转发消息给目标用户
//            System.out.println("转发订单数据给用户 " + targetId);
        } else {
            System.out.println("目标用户 " + targetId + " 未连接或连接已关闭");
        }

        // 获取当前用户的 userId
        String userId = (String) session.getAttributes().get("userId");

        // 转发给管理员，排除自己
        String userRole = (String) session.getAttributes().get("role");
        for (WebSocketSession adminSession : adminSessions.values()) {
            if (adminSession.isOpen() && !adminSession.getId().equals(session.getId()) && "ADMIN".equals(userRole)) {
                sendMessage(adminSession, payload); // 也将消息发送给所有管理员
//                System.out.println("用户 " + userId + " 转发订单数据给管理员 " + adminSession.getId());
            }
        }
    }

    // 发送消息
    private void sendMessage(WebSocketSession session, Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            System.err.println("发送消息时发生IO异常: " + e.getMessage());
            try {
                session.close(); // 发送失败时关闭会话
            } catch (IOException closeException) {
                System.err.println("关闭会话时发生异常: " + closeException.getMessage());
            }
        }
    }

    private Map<String, Object> parseMessage(String message) throws IOException {
        return objectMapper.readValue(message, HashMap.class);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        System.out.println("用户断开连接: " + session.getId() + ", 状态: " + status);

        // 移除断开连接的用户
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            userSessions.remove(userId);
            adminSessions.remove(userId); // 从管理员队列中移除
//            System.out.println("用户 " + userId + " 已从队列中移除");
        }
    }
}
