package org.ruoyi.controller;


import org.ruoyi.common.minio.util.MinIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.minio.messages.Item;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-18 15-49
 * @description
 */
@RestController
@RequestMapping("/api/minio")
public class MinIOController {

    @Autowired
    private MinIOUtil minioUtil;

    /**
     * 简单文件上传
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, String> fileInfo = minioUtil.uploadFile(file);
            if (fileInfo != null) {
                result.put("code", 200);
                result.put("message", "上传成功");
                result.put("data", fileInfo);
                return ResponseEntity.ok(result);
            } else {
                result.put("code", 500);
                result.put("message", "上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "上传异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 批量文件上传
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, String>> fileInfos = minioUtil.uploadFiles(files);
            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", fileInfos);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "上传异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName) {
        try {
            InputStream inputStream = minioUtil.downloadFile(fileName);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 文件预览
     */
    @GetMapping("/preview/{fileName}")
    public ResponseEntity<Map<String, Object>> previewFile(@PathVariable("fileName") String fileName) {
        Map<String, Object> result = new HashMap<>();
        try {
            String url = minioUtil.getObjectUrl(fileName, 1);
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("url", url);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable("fileName") String fileName) {
        Map<String, Object> result = new HashMap<>();
        try {
            minioUtil.deleteFile(fileName);
            result.put("code", 200);
            result.put("message", "删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 列出所有文件
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listFiles() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Item> items = minioUtil.listObjects();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", items);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 文件秒传检查
     */
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            String fileHash = minioUtil.generateFileHash(file);
            // 这里应该查询数据库或缓存，检查是否存在相同哈希值的文件
            // 为简化示例，直接返回不存在
            boolean exists = false;

            result.put("code", 200);
            result.put("message", "检查成功");
            result.put("exists", exists);
            result.put("fileHash", fileHash);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "检查异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
