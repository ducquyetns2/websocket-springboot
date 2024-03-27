package com.ducquyet.websocket.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

public class StoreServiceTest {
    private StoreService storeService;
    @BeforeEach
    public void init() {
        storeService=new StoreService();
    }
    public void save_file_return_path() {
    }
}
