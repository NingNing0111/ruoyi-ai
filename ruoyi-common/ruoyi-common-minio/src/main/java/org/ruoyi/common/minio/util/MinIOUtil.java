package org.ruoyi.common.minio.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-18 15-49
 * @description
 */
@Component
public class MinIOUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String defaultBucketName;

    /**
     * 检查存储桶是否存在
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取所有存储桶
     * @return 存储桶列表
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 删除存储桶
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 简单文件上传
     * @param file       文件
     * @param bucketName 存储桶名称
     * @return 文件信息
     */
    @SneakyThrows
    public Map<String, String> uploadFile(MultipartFile file, String bucketName) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("fileName", fileName);
        resultMap.put("originalFilename", originalFilename);
        resultMap.put("url", getObjectUrl(bucketName, fileName, 7));

        return resultMap;
    }

    /**
     * 简单文件上传（使用默认存储桶）
     * @param file 文件
     * @return 文件信息
     */
    public Map<String, String> uploadFile(MultipartFile file) {
        return uploadFile(file, defaultBucketName);
    }

    /**
     * 批量文件上传
     * @param files      文件列表
     * @param bucketName 存储桶名称
     * @return 文件信息列表
     */
    public List<Map<String, String>> uploadFiles(List<MultipartFile> files, String bucketName) {
        return files.stream()
                .map(file -> uploadFile(file, bucketName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 批量文件上传（使用默认存储桶）
     * @param files 文件列表
     * @return 文件信息列表
     */
    public List<Map<String, String>> uploadFiles(List<MultipartFile> files) {
        return uploadFiles(files, defaultBucketName);
    }

    /**
     * 下载文件
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 输入流
     */
    @SneakyThrows
    public InputStream downloadFile(String bucketName, String objectName) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 下载文件（使用默认存储桶）
     * @param objectName 对象名称
     * @return 输入流
     */
    public InputStream downloadFile(String objectName) {
        return downloadFile(defaultBucketName, objectName);
    }

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     */
    @SneakyThrows
    public void deleteFile(String bucketName, String objectName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 删除文件（使用默认存储桶）
     * @param objectName 对象名称
     */
    public void deleteFile(String objectName) {
        deleteFile(defaultBucketName, objectName);
    }

    /**
     * 批量删除文件
     * @param bucketName  存储桶名称
     * @param objectNames 对象名称列表
     * @return 删除错误列表
     */
    @SneakyThrows
    public List<DeleteError> deleteFiles(String bucketName, List<String> objectNames) {
        List<DeleteObject> objects = objectNames.stream()
                .map(DeleteObject::new)
                .collect(Collectors.toList());

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(objects)
                .build());

        List<DeleteError> errors = new ArrayList<>();
        for (Result<DeleteError> result : results) {
            errors.add(result.get());
        }
        return errors;
    }

    /**
     * 批量删除文件（使用默认存储桶）
     * @param objectNames 对象名称列表
     * @return 删除错误列表
     */
    public List<DeleteError> deleteFiles(List<String> objectNames) {
        return deleteFiles(defaultBucketName, objectNames);
    }

    /**
     * 获取文件URL
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expires    过期时间（天）
     * @return 文件URL
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName, int expires) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expires, TimeUnit.DAYS)
                .build());
    }

    /**
     * 获取文件URL（使用默认存储桶）
     * @param objectName 对象名称
     * @param expires    过期时间（天）
     * @return 文件URL
     */
    public String getObjectUrl(String objectName, int expires) {
        return getObjectUrl(defaultBucketName, objectName, expires);
    }

    /**
     * 检查文件是否存在
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 是否存在
     */
    @SneakyThrows
    public boolean objectExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件是否存在（使用默认存储桶）
     * @param objectName 对象名称
     * @return 是否存在
     */
    public boolean objectExists(String objectName) {
        return objectExists(defaultBucketName, objectName);
    }

    /**
     * 列出存储桶中的所有对象
     * @param bucketName 存储桶名称
     * @return 对象列表
     */
    @SneakyThrows
    public List<Item> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .build());

        List<Item> items = new ArrayList<>();
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }

    /**
     * 列出存储桶中的所有对象（使用默认存储桶）
     * @return 对象列表
     */
    public List<Item> listObjects() {
        return listObjects(defaultBucketName);
    }


    @SneakyThrows
    public String generateFileHash(MultipartFile file) {
        // 这里使用简单的文件大小和修改时间作为哈希值，实际应用中应使用MD5或SHA-1等算法
        return file.getSize() + "-" + file.getOriginalFilename();
    }

    public String getDefaultBucketName() {
        return defaultBucketName;
    }
}

