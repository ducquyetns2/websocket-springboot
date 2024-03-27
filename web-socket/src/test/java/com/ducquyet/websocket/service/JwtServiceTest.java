package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.User;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class JwtServiceTest {
    private JwtService jwtService;
    @BeforeEach
    void init() {
    }
    @Test
    void generateTokenWithFakeUser() {
    }
}
