package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.Conversation;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.ConversationRepository;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.requestDto.ConversationRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }
    public List<Conversation> getConversationsByUsername(String username) {
        return conversationRepository.findByUsers_Username(username);
    }
    public Conversation createConversation(ConversationRegister register) {
        List<User> users=userRepository.findByUsernameIn(register.getMembers());
        return Conversation.builder()
                .users(new HashSet<>(users))
                .isGroup(register.isGroup())
                .build();
    }
    public Conversation getOrCreateConversationByMembers(String member1,String member2) {
        List<User> users=userRepository.findByUsernameIn(Set.of(member1,member2));
        if(users.isEmpty() || users.size() > 2) throw new UsernameNotFoundException("Username are not invalid");
        Optional<Conversation> conversation=conversationRepository.findByMember1AndMember2(users.get(0),users.get(1));
        if(conversation.isPresent()) return conversation.get();
        else {
            Conversation newConversation=new Conversation();
            newConversation.setUsers(new HashSet<>(users));
            newConversation.setGroup(false);
            conversationRepository.save(newConversation);
            return newConversation;
        }
    }
}
