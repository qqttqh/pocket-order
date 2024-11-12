// src/utils/ossUtil.js
import axios from 'axios';
import OSS from 'ali-oss';
import { v4 as uuidv4 } from 'uuid';
import {ElMessage} from "element-plus"; // 导入 uuid 库

export const uploadFileToOSS = async (file) => {
    if (!file) {
        throw new Error('未提供文件');
    }

    try {
        // 1: 获取 OSS 签名
        const { data: policyData } = await axios.get('/api/oss/policy');

        // 2: 创建 OSS 客户端
        const client = new OSS({
            region: 'cn-guangzhou',
            accessKeyId: policyData.accessId,
            accessKeySecret: 'vpret83wVMfV1Om0sxy9o0PjpFfk7J',
            bucket: 'xyquan',
            endpoint: 'oss-cn-guangzhou.aliyuncs.com',
        });

        // 3: 生成唯一的文件名
        const fileExtension = file.name.split('.').pop(); // 获取文件扩展名
        const uniqueFileName = `${policyData.dir}${uuidv4()}.${fileExtension}`; // 使用 UUID 生成文件名

        // 4: 上传文件到 OSS
        const result = await client.put(uniqueFileName, file);

        //5: 上传成功后返回文件的完整 URL
        return result.url;
    } catch (error) {
        ElMessage.error('图片上传失败');
        console.error('上传到 OSS 失败:', error);
        throw error;
    }
};
