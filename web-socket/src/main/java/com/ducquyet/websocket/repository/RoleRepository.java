package com.ducquyet.websocket.repository;

import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Set<Role> findByRoleIn(Set<RoleEnum> roles);
    Optional<Role> findByRole(RoleEnum role);
}
