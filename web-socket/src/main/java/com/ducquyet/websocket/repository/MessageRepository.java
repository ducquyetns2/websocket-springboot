package com.ducquyet.websocket.repository;

import com.ducquyet.websocket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    List<Message> findByConversation_Id(Integer id);
}
