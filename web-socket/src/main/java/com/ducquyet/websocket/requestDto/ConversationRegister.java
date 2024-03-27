package com.ducquyet.websocket.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRegister {
    @NotNull
    private Set<String> members;
    private boolean isGroup;
}
