package com.ducquyet.websocket.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRegister {
    private Integer conversationId;
    private String sender;
    private String message;
}
