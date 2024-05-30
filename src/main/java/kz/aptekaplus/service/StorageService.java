package kz.aptekaplus.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import kz.aptekaplus.exception.server.InternalServerErrorException;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public interface StorageService {
    String uploadFile(String filePath, InputStream inputStream);
    byte[] getFile(String filePath);
    String renameFile(String oldPath, String newPath);
    void deleteFile(String path);

    String getFileUrl(String filePath) ;

}
