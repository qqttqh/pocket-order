package top.xyquan.pocketorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xyquan.pocketorderservice.service.impl.OSSService;

import java.util.Map;

@RestController
@RequestMapping("/oss")
public class OSSController {
    @Autowired
    private OSSService ossService;

    @GetMapping("/policy")
    public ResponseEntity<Map<String, String>> getUploadPolicy() {
        Map<String, String> policy = ossService.generateUploadPolicy();
        return ResponseEntity.ok(policy);
    }
}

