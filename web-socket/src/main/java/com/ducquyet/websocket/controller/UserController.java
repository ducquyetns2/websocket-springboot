package com.ducquyet.websocket.controller;

import responseDto.UserDto;
import com.ducquyet.websocket.requestDto.UserRegister;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.service.StoreService;
import com.ducquyet.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StoreService storeService;
    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/users/online")
    ResponseEntity<List<UserDto>> getOnlineUser() {
        return new ResponseEntity<>(userService.getOnlineUsers(), HttpStatus.OK);
    }
    @PostMapping(value = "/users",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> createUser(@ModelAttribute UserRegister userRegister) throws IOException {
        return ResponseEntity.ok(userService.save(userRegister,userRegister.getAvatar()));
    }
    @GetMapping("/users/{id}")
    ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }
    @GetMapping("/users/image/{image}")
    ResponseEntity<Path> getImage(@PathVariable String image) {
        return new ResponseEntity<>(storeService.load(image),HttpStatus.OK);
    }
    @GetMapping("/users/username/{username}")
    ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
    }
}
