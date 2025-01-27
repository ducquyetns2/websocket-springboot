package com.ducquyet.websocket.configuration;

import com.ducquyet.websocket.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE+99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;
    @Value("${client.origin}")
    private String clientOrigin;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic","/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
                StompHeaderAccessor accessor=MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
                assert accessor != null;
                if(StompCommand.CONNECT.equals( accessor.getCommand())) {
                    String authHeader=accessor.getFirstNativeHeader("Authorization");
                    if(authHeader==null || ! authHeader.startsWith("Bearer ")) {
                        throw new AccessDeniedException("UNAUTHORIZED_MESSAGE");
                    }
                    String token=authHeader.substring(7);
                    if(!jwtService.validateToken(token)) {
                        throw new JwtException("Token has been expired");
                    }
                    UserDetails userDetails= jwtService.getUserDetails(token);
                    Authentication authentication=new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    accessor.setUser(authentication);
                    accessor.setLeaveMutable(false);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                return ChannelInterceptor.super.preSend(message, channel);
            }
        });
    }
}
