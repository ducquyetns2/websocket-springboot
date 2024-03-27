package com.ducquyet.websocket.requestDto;

import com.ducquyet.websocket.entity.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Getter
@Data
public class AuthenticationRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Set<RoleEnum> roles;
}
