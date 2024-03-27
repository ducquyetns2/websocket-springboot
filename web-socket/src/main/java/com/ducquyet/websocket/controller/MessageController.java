package com.ducquyet.websocket.controller;

import com.ducquyet.websocket.entity.Message;
import com.ducquyet.websocket.requestDto.MessageRegister;
import com.ducquyet.websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class MessageController {
    private final MessageService messageService;
    @GetMapping("/messages")
    ResponseEntity<List<Message>> getMessages() {
        return new ResponseEntity<>(messageService.getALlMessages(), HttpStatus.OK);
    }
    @PostMapping("/messages")
    ResponseEntity<Message> getMessages(@RequestBody MessageRegister messageRegister) {
        return new ResponseEntity<>(messageService.createMessage(messageRegister), HttpStatus.OK);
    }
    @GetMapping("/conversations/{conversationId}/messages")
    ResponseEntity<List<Message>> getMessagesByConversationId(@PathVariable Integer conversationId) {
        return new ResponseEntity<>(messageService.getMessagesByConversationId(conversationId),HttpStatus.OK);
    }
}
