package com.ducquyet.websocket.repository;

import com.ducquyet.websocket.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByIsOnline(boolean isOnline);
    List<User> findByUsernameIn(Set<String> usernames);
}
