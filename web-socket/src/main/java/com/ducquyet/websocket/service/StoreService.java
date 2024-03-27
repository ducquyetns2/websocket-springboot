package com.ducquyet.websocket.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StoreService {
    private final Path locationPath;
    public StoreService() {
        String location = "src/main/resources/static/uploads";
        locationPath=Paths.get(location);
    }
    public void init() throws IOException {
        if(!Files.exists(locationPath)) {
            Files.createDirectories(locationPath);
        }
    }
    public void deleteAll() throws IOException {
        FileSystemUtils.deleteRecursively(locationPath);
    }
    public Path store(MultipartFile files) throws IOException {
        String newFilename=this.changeFilename(files);
        Path newFilesPath=locationPath.resolve(newFilename).normalize().toAbsolutePath();
        Files.copy(files.getInputStream(),newFilesPath,StandardCopyOption.REPLACE_EXISTING);
        return newFilesPath;
    }
    private String changeFilename(MultipartFile files) {
        String originalFilename=files.getOriginalFilename();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename=originalFilename.substring(0,originalFilename.lastIndexOf("."));
        return filename+UUID.randomUUID()+extension;
    }
    public Path load(String filename) {
        return this.locationPath.resolve(filename);
    }
    public byte[] loadAsByte(String filename) throws IOException {
        return StreamUtils.copyToByteArray(loadAsResource(filename).getInputStream());
    }
    public Resource loadAsResource(String filename) throws MalformedURLException {
        return new UrlResource(this.load(filename).toUri());
    }
    public Stream<Path> loadAll() throws IOException {
        return Files.walk(locationPath,1).map(locationPath::relativize);
    }
}
