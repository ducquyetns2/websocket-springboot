package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.Token;
import com.ducquyet.websocket.repository.TokenRepository;
import com.ducquyet.websocket.requestDto.AuthenticationRequest;
import com.ducquyet.websocket.requestDto.UserRegister;
import com.ducquyet.websocket.entity.Role;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.RoleRepository;
import com.ducquyet.websocket.repository.UserRepository;
import com.ducquyet.websocket.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import responseDto.AuthenticationResponse;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public void register(UserRegister userRegister) {
        Set<Role> roles=roleRepository.findByRoleIn(userRegister.getRoles());
        User user=User.builder()
                .fullName(userRegister.getFullName())
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);
    }
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication=new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.generateToken(user,jwtService.generateClaims(user));
        user.setOnline(true);
        Token tokenEntity=Token.builder()
                .accessToken(token)
                .user(user)
                .isRevoked(false)
                .build();
        tokenRepository.save(tokenEntity);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .avatar(user.getAvatar())
                .roles(user.getRoles())
                .accessToken(token)
                .build();
    }
    public void logout(String username) {
        List<Token> tokenList=tokenRepository.findByUser_Username(username);
        tokenList.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(tokenList);
        User user=userRepository.findByUsername(username).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);
    }
}
