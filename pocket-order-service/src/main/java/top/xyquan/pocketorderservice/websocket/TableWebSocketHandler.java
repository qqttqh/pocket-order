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

public class TableWebSocketHandler extends TextWebSocketHandler {

    // 用于存储所有桌子的信息
    private final Map<String, Table> tables = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 获取用户ID和桌子ID参数
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            String[] userIdHolder = new String[1]; // 用数组来存储 userId
            String[] tableIdHolder = new String[1]; // 用数组来存储 tableId

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if ("userId".equals(keyValue[0])) {
                            userIdHolder[0] = keyValue[1]; // 获取 userId 参数
                        } else if ("tableId".equals(keyValue[0])) {
                            tableIdHolder[0] = keyValue[1]; // 获取 tableId 参数
                        }
                    }
                }
            }

            // 确保 userId 和 tableId 都提供了
            if (userIdHolder[0] != null && tableIdHolder[0] != null) {
                session.getAttributes().put("userId", userIdHolder[0]); // 将 userId 存入 session 属性
                session.getAttributes().put("tableId", tableIdHolder[0]); // 将 tableId 存入 session 属性
//                System.out.println("用户 " + userIdHolder[0] + " 已连接到桌子 " + tableIdHolder[0]);

                // 将用户加入对应的桌子的会话队列
                tables.computeIfAbsent(tableIdHolder[0], k -> new Table(tableIdHolder[0])).addSession(session);

                // 如果是第二个或之后的用户加入，发送请求消息给第一个用户
                Table table = tables.get(tableIdHolder[0]);
                if (table.getSessionCount() > 1) {
                    WebSocketSession firstSession = table.getFirstSession();
                    Map<String, Object> requestMessage = new HashMap<>();
                    requestMessage.put("type", "request-dishes-data");
                    requestMessage.put("requestId", userIdHolder[0]); // 将新加入用户的 userId 作为 requestId
                    sendMessage(firstSession, requestMessage);
                }
            } else {
                session.close(); // 如果未提供 userId 或 tableId，关闭连接
                System.err.println("连接时未提供 userId 或 tableId 参数，关闭连接");
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // 解析消息
            Map<String, Object> payload = parseMessage(message.getPayload());
            // 获取消息类型
            String type = (String) payload.get("type");
            // 获取当前用户的 tableId
            String tableId = (String) session.getAttributes().get("tableId");

            switch (type) {
                case "add-count": // 增加计数
                case "reduce-count": // 减少菜品计数
                case "clear-count": // 清空点餐数据
                case "switch-checked": // 切换菜品选择状态
                case "checked-all":  // 全选
                case "unchecked-all": // 取消全选
                case "ordered": // 已下单成功
                    broadcastToTable(payload, tableId, session);
                    break;
                case "response-dishes-data": // 响应点餐数据给新用户
                    handleResponseDishesData(payload, session);
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


    // 处理用户响应的菜品数据，转发给目标用户
    private void handleResponseDishesData(Map<String, Object> payload, WebSocketSession session) throws IOException {
        String targetId = (String) payload.get("target");

        // 获取当前用户的 userId 和 tableId
        String userId = (String) session.getAttributes().get("userId");
        String tableId = (String) session.getAttributes().get("tableId");

        // 获取当前桌子的信息
        Table table = tables.get(tableId);
        if (table != null) {
            // 遍历当前桌子的会话，寻找目标用户
            for (WebSocketSession targetSession : table.getSessions()) {
                String sessionUserId = (String) targetSession.getAttributes().get("userId");
                if (targetId.equals(sessionUserId)) {
                    // 发送响应数据给目标用户
                    sendMessage(targetSession, payload);
//                    System.out.println("转发响应菜品数据给用户 " + targetId);
                    return; // 找到目标用户后返回
                }
            }
        } else {
            System.err.println("桌子 " + tableId + " 不存在，无法转发消息");
        }
    }


    // 广播加菜和减菜消息
    private void broadcastToTable(Map<String, Object> payload, String tableId, WebSocketSession senderSession) throws IOException {
        // 获取对应桌子的信息
        Table table = tables.get(tableId);
        if (table != null) {
            for (WebSocketSession session : table.getSessions()) {
                // 检查当前会话是否是发送者，跳过发送给自己
                if (!session.getId().equals(senderSession.getId())) {
                    sendMessage(session, payload); // 将消息发送给桌子内所有用户（不包括自己）
                }
            }
        }
    }


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

        // 从所有桌子的会话队列中移除该会话
        for (Table table : tables.values()) {
            table.removeSession(session);
        }
    }
}
