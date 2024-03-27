package com.ducquyet.websocket.service;

import responseDto.UserDto;
import com.ducquyet.websocket.requestDto.UserRegister;
import com.ducquyet.websocket.controller.ResourceController;
import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.entity.RoleEnum;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.RoleRepository;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StoreService storeService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserUtils userUtils;
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userUtils::mapToUserDto)
                .collect(Collectors.toList());
    }
    public List<UserDto> getOnlineUsers() {
        return userRepository.findByIsOnline(true)
                .stream().map(userUtils::mapToUserDto)
                .collect(Collectors.toList());
    }
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(new User());
    }
    public User getUserByUsername(String username) {
        Optional<User> user=userRepository.findByUsername(username);
        return userRepository.findByUsername(username).orElseThrow();
    }
    public User save(UserRegister userRegister, MultipartFile files) throws IOException {
       Path avatarPath=storeService.store(files);
       Set<Role> roles=roleRepository.findByRoleIn(userRegister.getRoles());
       if(roles.isEmpty()) {
           roles= Set.of(roleRepository.findByRole(RoleEnum.USER).orElseThrow());
       }
        String avatarUrl=MvcUriComponentsBuilder.fromMethodName(ResourceController.class,"readFileAsByte",avatarPath.getFileName().toString())
                .build()
                .toUri()
                .toString();
        User newUser=User.builder()
                .fullName(userRegister.getFullName())
                .username(userRegister.getUsername())
                .password( passwordEncoder.encode(userRegister.getPassword()) )
                .avatarUrl(avatarUrl)
                .avatar(avatarPath.getFileName().toString())
                .roles(roles)
                .build();
       userRepository.save(newUser);
       return newUser;
    }
}
