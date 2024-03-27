package com.ducquyet.websocket.controller;

import com.ducquyet.websocket.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class ResourceController {
    private final StoreService storeService;
    @GetMapping("/images/{filename}")
    public ResponseEntity<Path> readFile(@PathVariable String filename) {
        return ResponseEntity.ok(storeService.load(filename));
    }
    @GetMapping("/images/resource/{filename}")
    public ResponseEntity<Resource> readFileAsResource(@PathVariable String filename) throws MalformedURLException {
        Resource resource=storeService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+resource.getFilename()+"\"").body(resource);
    }
    @GetMapping("/images/byte/{filename}")
    public ResponseEntity<byte[]> readFileAsByte(@PathVariable String filename) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(storeService.loadAsByte(filename));
    }
}
