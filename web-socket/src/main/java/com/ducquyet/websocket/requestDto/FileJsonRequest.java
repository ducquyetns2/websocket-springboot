package com.ducquyet.websocket.requestDto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileJsonRequest {
    private MultipartFile file;
    private String jsonData;
}
