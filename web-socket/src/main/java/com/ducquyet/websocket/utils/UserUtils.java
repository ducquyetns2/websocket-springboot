package com.ducquyet.websocket.utils;

import responseDto.UserDto;
import com.ducquyet.websocket.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .avatar(user.getAvatar())
                .isOnline(user.isOnline())
                .build();
    }
}
