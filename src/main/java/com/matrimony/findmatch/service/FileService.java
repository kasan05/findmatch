package com.matrimony.findmatch.service;

import jakarta.servlet.ServletOutputStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    public void upload(MultipartFile[] files,String userId) throws IOException {
        for(MultipartFile file : files){
            Path path = Paths.get(
                    "/Users/kasankulasegaram/kasanJob/Immigration docs/findmatch/fileuploads",
                    "FOR_VERIFICATION",
                    userId,
                    file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void uploadPhotoAlbum(List<MultipartFile> files,String userId) throws IOException {
        for(MultipartFile file : files) {
            Path path = Paths.get("/Users/kasankulasegaram/kasanJob/Immigration docs/findmatch/fileuploads",
                    "PHOTO_ALBUM",
                    userId,
                    file.getOriginalFilename()
                    );
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public List<Resource> getFilesByUserId(String userId) throws IOException {
            Path directory = Paths.get("/Users/kasankulasegaram/kasanJob/Immigration docs/findmatch/fileuploads",
                    "PHOTO_ALBUM",
                    userId);
        List<Resource> resources;
        try(Stream<Path> stream= Files.walk(directory)){
            resources = stream.filter(Files::isRegularFile)
                    .map(FileSystemResource::new)
                    .collect(Collectors.toUnmodifiableList());
        }
        return resources;
    }
    public void getFilesByUserId2(ServletOutputStream outputStream,
            String userId) throws IOException {
        Path directory = Paths.get("/Users/kasankulasegaram/kasanJob/Immigration docs/findmatch/fileuploads",
                "PHOTO_ALBUM",
                userId);
            try (Stream<Path> stream = Files.list(directory)) {
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                stream.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                                zipOutputStream.putNextEntry(zipEntry);
                                Files.copy(path, zipOutputStream);
                                zipOutputStream.closeEntry();
                            } catch (IOException e) {
                                throw new RuntimeException("Error writing file: " + path, e);
                            }
                        });
                zipOutputStream.finish();;
                zipOutputStream.flush();

            } catch (IOException e) {
                throw new RuntimeException("Error walking directory", e);
            }

    }
}