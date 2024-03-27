package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.Conversation;
import com.ducquyet.websocket.entity.Message;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.ConversationRepository;
import com.ducquyet.websocket.repository.MessageRepository;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.requestDto.MessageRegister;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
        private final MessageRepository messageRepository;
        private final UserRepository userRepository;
        private final ConversationRepository conversationRepository;
        public List<Message> getALlMessages() {
            return messageRepository.findAll();
        }
        public List<Message> getMessagesByConversationId(Integer conversationId) {
            return messageRepository.findByConversation_Id(conversationId);
        }
        public Message createMessage(MessageRegister messageRegister) {
            Conversation conversation=conversationRepository.findById(messageRegister.getConversationId().intValue());
            Optional<User> user=userRepository.findByUsername(messageRegister.getSender());
            if(user.isEmpty()) throw new UsernameNotFoundException("sender does not exist");
            Message newMessage=Message.builder()
                    .conversation(conversation)
                    .message(messageRegister.getMessage())
                    .sender(user.get())
                    .build();
            messageRepository.save(newMessage);
            return newMessage;
        }
}
