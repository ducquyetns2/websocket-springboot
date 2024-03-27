package com.ducquyet.websocket.controller;


import com.ducquyet.websocket.entity.Conversation;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.requestDto.ConversationRegister;
import com.ducquyet.websocket.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConversationController {
    private final ConversationService conversationService;
    @GetMapping("/conversations")
    ResponseEntity< List<Conversation> > getConversations(Authentication authentication) {
        return new ResponseEntity<>(conversationService.getConversationsByUsername(authentication.getName()), HttpStatus.OK);
    }
    @GetMapping("/conversations/{username}")
    ResponseEntity< List<Conversation> > getConversationsByUsername(@PathVariable(name="username") String username) {
        return new ResponseEntity<>(conversationService.getConversationsByUsername(username), HttpStatus.OK);
    }
    @GetMapping("/conversations/{member1}/{member2}")
    ResponseEntity<Conversation> getOrCreateConversationsByMembers(@PathVariable("member1") String member1,@PathVariable("member2") String member2) {
        return new ResponseEntity<>(conversationService.getOrCreateConversationByMembers(member1,member2), HttpStatus.OK);
    }
    @PostMapping("/conversations/{member}")
    ResponseEntity<Conversation> getOrCreateConversationsByMember(@PathVariable String member,Authentication authentication) {
        return new ResponseEntity<>(conversationService.getOrCreateConversationByMembers(authentication.getName(),member), HttpStatus.OK);
    }
    @PostMapping("/conversations")
    ResponseEntity<Conversation> createConversation(@RequestBody ConversationRegister conversationRegister) {
        return new ResponseEntity<>(conversationService.createConversation(conversationRegister), HttpStatus.CREATED);
    }
}
