package com.ducquyet.websocket.repository;

import com.ducquyet.websocket.entity.Conversation;
import com.ducquyet.websocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Integer> {
    List<Conversation> findByUsers_Username(String username);
    @Query("select c from Conversation c where :member1 Member of c.users and :member2 Member of c.users")
    Optional<Conversation> findByMember1AndMember2(User member1, User member2);
    Conversation findById(int id);
}
