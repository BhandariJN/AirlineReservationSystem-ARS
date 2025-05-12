package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.config;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.url}")
    private String endpoint;
    @Value("${minio.bucket-name}")
    private String bucketName;


    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Minio client", e);
        }
    }

    public void bucketExists() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        try {
            boolean exists = minioClient().bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName).build());
            if (!exists) {
                minioClient().makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Bucket check/creation failed", e);
        }
    }

    public void UploadFile(MultipartFile file, String uuid) {
        try {
            bucketExists();
            minioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuid)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Minio", e);
        }
    }

   public String getPresignedUrl(String objectName) {
        try {
            bucketExists();
            return minioClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating presigned URL"+ e.getMessage());
        }
    }

    public void deleteFile(String objectName) {
        try {
            bucketExists();
            minioClient().removeObject(
                    RemoveObjectArgs.builder()
                            .bucket("{bucketName}")
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from Minio", e);
        }
    }


    public void updateFile(MultipartFile file, String objectName) {
        try {
            bucketExists();
            minioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket("{bucketName}")
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error updating file in Minio", e);
        }
    }


}
