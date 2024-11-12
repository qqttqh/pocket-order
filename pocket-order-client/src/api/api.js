import axios from 'axios';
import Cookies from 'js-cookie';

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
});

// 请求拦截器
api.interceptors.request.use(
    (config) => {
        // 这里可以添加请求头等操作，例如添加token
        const token = Cookies.get('token'); // 从 Cookies 获取 token
        const uuid = localStorage.getItem('user_uuid');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`; // 将 token 添加到请求头
        }
        if(uuid){
            config.headers['uuid'] = `${uuid}`;
        }

        return config;
    },
    (error) => {
        // 处理请求错误
        return Promise.reject(error);
    }
);

// 响应拦截器
api.interceptors.response.use(
    (response) => {
        // 检查响应数据并处理 token
        const { data } = response;
        if (data && data.token) {
            // 存储 token 到 Cookies
            Cookies.set('token', data.token, { expires: 15 }); // 设定过期时间为15天
        }
        return response; // 返回响应数据
    },
    (error) => {
        // 处理响应错误
        return Promise.reject(error);
    }
);

export default api;
