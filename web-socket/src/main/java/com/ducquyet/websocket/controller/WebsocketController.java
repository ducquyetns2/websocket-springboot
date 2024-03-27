package com.ducquyet.websocket.controller;

import com.ducquyet.websocket.entity.Message;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.requestDto.MessageRegister;
import com.ducquyet.websocket.service.MessageService;
import com.ducquyet.websocket.service.UserService;
import com.ducquyet.websocket.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import responseDto.UserDto;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WebsocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate template;
    private final UserUtils userUtils;
    private final UserRepository userRepository;
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRegister messageRegister) {
        Message message=messageService.createMessage(messageRegister);
        Set<User> otherUsers=message.getConversation().getUsers();
        otherUsers.forEach(user -> {
            template.convertAndSendToUser(user.getUsername(),"/queue/chat.receiveMessage",message);
        });
    }
    @MessageMapping("/chat.online")
    @SendTo("/topic/chat.online")
    public UserDto getNotify(Principal principal) {
        User user=userRepository.findByUsername(principal.getName()).get();
        return userUtils.mapToUserDto(user);
    }
    @MessageMapping("/test")
    public void test1() {
        template.convertAndSend("/topic/chat.offline","Has bean log");
    }
}
