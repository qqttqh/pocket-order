package top.xyquan.pocketorderservice.websocket;

import org.springframework.web.socket.WebSocketSession;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableId; // 桌子 ID
    private List<WebSocketSession> sessions; // 存放该桌子的 WebSocketSession 队列

    // 构造方法
    public Table(String tableId) {
        this.tableId = tableId;
        this.sessions = new ArrayList<>();
    }

    // 获取桌子 ID
    public String getTableId() {
        return tableId;
    }

    // 添加 session
    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    // 移除 session
    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // 获取桌子所有的 session
    public List<WebSocketSession> getSessions() {
        return sessions;
    }

    // 获取桌子的第一个 session
    public WebSocketSession getFirstSession() {
        return sessions.isEmpty() ? null : sessions.get(0);
    }

    // 获取当前桌子 session 数量
    public int getSessionCount() {
        return sessions.size();
    }
}
