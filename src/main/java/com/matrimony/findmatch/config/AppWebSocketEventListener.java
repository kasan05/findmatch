package com.matrimony.findmatch.config;

import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppWebSocketEventListener {

    private final Map<Long, String> activeUsers = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent sessionConnectedEvent){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        String username = accessor.getUser() != null ? accessor.getUser().getName() : "Anonymous";
        String sessionId = accessor.getSessionId();
        User user =userService.getUserByEmail(username).orElse(null);
        if (user != null) {
            activeUsers.put(user.getId(), sessionId);
        }
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        // Find and remove the user by session ID
        activeUsers.values().remove(sessionId);
        System.out.println("User disconnected with session: " + sessionId);
    }

    public boolean isUserOnline(String username) {
        return activeUsers.containsKey(username);
    }

    public List<Long> getActiveUsers() {
        return activeUsers.keySet().stream().toList();
    }
}
