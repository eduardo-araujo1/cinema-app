package com.eduardo.cinema_app.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImg(MultipartFile multipartFile) {
        String filename = generateFileName(multipartFile);

        try {
            uploadFileToS3(multipartFile, filename);
            return getFileUrl(filename);
        } catch (Exception e) {
            log.error("Erro ao subir arquivo: {}", e.getMessage());
            return "";
        }
    }

    private String generateFileName(MultipartFile multipartFile) {
        return UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
    }

    private void uploadFileToS3(MultipartFile multipartFile, String filename) throws IOException {
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .contentType(multipartFile.getContentType())
                .metadata(Map.of("Content-Disposition", "inline"))
                .build();

        s3Client.putObject(putOb, RequestBody.fromByteBuffer(ByteBuffer.wrap(multipartFile.getBytes())));
    }

    private String getFileUrl(String filename) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        return s3Client.utilities().getUrl(request).toString();
    }
}
