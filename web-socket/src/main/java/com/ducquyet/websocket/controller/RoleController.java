package com.ducquyet.websocket.controller;

import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.repository.RoleRepository;
import com.ducquyet.websocket.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="api/v1")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping("/roles")
      ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleService.getALlRoles(), HttpStatus.OK);
    }
}
