package com.ducquyet.websocket.requestDto;

import com.ducquyet.websocket.entity.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class UserRegister {
    @NotNull
    private String fullName;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private MultipartFile avatar;
    private Set<RoleEnum> roles;
}
