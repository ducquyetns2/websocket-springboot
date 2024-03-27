package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public List<Role> getALlRoles() {
        return roleRepository.findAll();
    }
}
