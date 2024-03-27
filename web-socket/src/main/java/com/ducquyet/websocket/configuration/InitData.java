package com.ducquyet.websocket.configuration;

import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.entity.RoleEnum;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.RoleRepository;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.service.StoreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleStatus;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;
    @PostConstruct
    void initUser() {
        Set<Role> roles=roleRepository.findByRoleIn(Set.of(RoleEnum.ADMIN,RoleEnum.USER,RoleEnum.EDITOR));
        if(!roles.isEmpty()) return;
        Role adminRole = Role.builder().role(RoleEnum.ADMIN).build();
        Role userRole = Role.builder().role(RoleEnum.USER).build();
        Role editorRole = Role.builder().role(RoleEnum.EDITOR).build();
        // Save all role in database
        roleRepository.saveAll(Set.of(adminRole,userRole,editorRole));
    }
    @Override
    public void run(String... args) throws Exception {
        var role=roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(ChangeSetPersister.NotFoundException::new);
        Optional<User> user=userRepository.findByUsername("ducquyetns2");
        if(user.isEmpty()) {
        User adminUser=User.builder()
                .fullName("Nguyen Duc Quyet")
                .username("ducquyetns2")
                .password(passwordEncoder.encode("Anhquy28"))
                .roles(Set.of(role))
                .build();
        userRepository.save(adminUser);
        }
        // init directory
        storeService.init();
    }
}
