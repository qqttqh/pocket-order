package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@RestController
//@RequestMapping("/upload")
public class FileUploadController {

    @Value("${file.upload-dir}") // 配置上传目录
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "文件不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 生成随机文件名
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String randomFileName = UUID.randomUUID().toString() + (fileExtension != null ? "." + fileExtension : "");
            Path uploadPath = Paths.get(uploadDir, randomFileName);

            // 确保目录存在
            Files.createDirectories(uploadPath.getParent());

            // 保存文件
            Files.copy(file.getInputStream(), uploadPath);
            response.put("message", "文件上传成功");
            response.put("uploadPath", "/api/uploads/" + randomFileName); // 返回文件访问路径
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "文件上传失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
