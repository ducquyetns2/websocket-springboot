package com.ducquyet.websocket.event;

import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.utils.UserUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebsocketEventListener {
    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private StompHeaderAccessor getAccessor(AbstractSubProtocolEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage());
    }
    @EventListener
    public void handleConnectedListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor=this.getAccessor(event);
        String username=accessor.getUser().getName();
        User user=userRepository.findByUsername(username).orElseThrow();
        user.setOnline(true);
        userRepository.save(user);
    }
    @EventListener
    public void handleDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor=this.getAccessor(event);
        User user=userRepository.findByUsername(accessor.getUser().getName()).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);
        accessor.setUser(null);
        SecurityContextHolder.getContext().setAuthentication(null);
        template.convertAndSend("/topic/chat.offline",userUtils.mapToUserDto(user));
    }
}
