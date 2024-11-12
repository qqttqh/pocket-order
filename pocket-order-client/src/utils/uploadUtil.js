import axios from 'axios';

export const uploadImage = async (file) => {
    try {
        // 创建 FormData 实例，并将文件添加到表单数据中
        const formData = new FormData();
        formData.append('file', file);

        // 发送 POST 请求上传图片
        const response = await axios.post('/api/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data', // 设置为多部分表单格式
            },
        });

        // 返回服务器响应
        return response.data.uploadPath;
    } catch (error) {
        console.error('图片上传失败:', error);
        throw error;
    }
};
