package top.xyquan.pocketorderservice.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xyquan.pocketorderservice.config.AliyunOSSConfig;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class OSSService {
    @Autowired
    private AliyunOSSConfig aliyunOSSConfig;

    public Map<String, String> generateUploadPolicy() {
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String bucket = aliyunOSSConfig.getBucketName();
        String dir = aliyunOSSConfig.getDir();
        String endpoint = aliyunOSSConfig.getEndpoint();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            long expireTime = 30 * 60; // 签名过期时间，单位为秒
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);

            // 签名策略
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            // 生成签名
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<>();
            respMap.put("accessId", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", "https://" + bucket + "." + endpoint);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            return respMap;
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        } finally {
            ossClient.shutdown();
        }
    }
}

