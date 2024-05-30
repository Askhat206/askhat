package kz.aptekaplus.service.impl;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import kz.aptekaplus.config.MinioProperties;
import kz.aptekaplus.exception.EntityNotFoundException;
import kz.aptekaplus.exception.server.InternalServerErrorException;
import kz.aptekaplus.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements StorageService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    @Override
    public String getFileUrl(String filePath) {
        try {
            throwExceptionIfFileDoesNotExists(filePath);

            log.info("Generating URL for file with file name: {}", filePath);

            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.bucketName())
                            .object(filePath)
                            .expiry(2, TimeUnit.DAYS)
                            .build()
            );

            log.info("Generated URL: {}", fileUrl);

            return fileUrl;
        } catch (EntityNotFoundException e) {
            log.warn("File not found, returning default message. Error: {}", e.getMessage());
            return "File not found: " + filePath;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to generate URL for file: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadFile(String filePath, InputStream inputStream) {
        try {
            log.info("Uploading file with file name: {}", filePath);
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(minioProperties.bucketName())
                            .object(filePath)
                            .build()
            );

            log.info("Uploaded file: {}", filePath);

            return objectWriteResponse.object();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getFile(String filePath) {
        try {
            throwExceptionIfFileDoesNotExists(filePath);

            log.info("Downloading file with file name: {}", filePath);

            GetObjectResponse getObjectResponse = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(filePath)
                            .build()
            );

            log.info("Downloaded file: {}", filePath);

            byte[] bytes = getObjectResponse.readAllBytes();
            return bytes;
        } catch (Exception ignored) {
//            throw new InternalServerErrorException("Failed to download file: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String renameFile(String oldPath, String newPath) {
        try {
            throwExceptionIfFileDoesNotExists(oldPath);

            log.info("Renaming file from {} to {}", oldPath, newPath);

            ObjectWriteResponse objectWriteResponse = minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(oldPath)
                            .source(
                                    CopySource.builder()
                                            .bucket(minioProperties.bucketName())
                                            .object(newPath)
                                            .build()
                            )
                            .build()
            );

            deleteFile(oldPath);

            log.info("File renamed from {} to {}", oldPath, newPath);

            return objectWriteResponse.object();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to rename file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            throwExceptionIfFileDoesNotExists(path);

            log.info("Deleting file with file name: {}", path);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(path)
                            .build()
            );

            log.info("Deleted file: {}", path);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    private void throwExceptionIfFileDoesNotExists(String fileName) throws Exception {
        StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(minioProperties.bucketName())
                        .object(fileName)
                        .build()
        );

        if (statObjectResponse == null) {
//            throw new EntityNotFoundException("File with name " + fileName + " does not exist");
        }
    }

    @PostConstruct
    private void createBucketIfDoesNotExists() throws Exception {
        String bucketName = minioProperties.bucketName();

        boolean isBucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!isBucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            log.warn("Created new backed with name: {}", bucketName);
        }
    }

}
