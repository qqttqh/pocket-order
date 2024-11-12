import { defineStore } from "pinia";
import {reactive, ref, watch} from "vue";
import { ElMessage } from "element-plus";
import { encryptPassword } from "@/utils/encriptUtil.js";
import api from "@/api/api.js";
import Cookies from 'js-cookie';
import { v4 as uuidv4 } from 'uuid';
import {useOrdersStore} from "@/stores/orders.js";

export const useUserStore = defineStore('userStore', () => {
    const data = reactive({
        id: null,
        username: '',
        avatar: '',
        integrate: 0,
        role: '',
        token: ''
    });

    // 初始化用户数据，检查本地存储是否有 uuid
    const initUserData = () => {
        let localUuid = localStorage.getItem('user_uuid');
        if (!localUuid) {
            localUuid = uuidv4();
            localStorage.setItem('user_uuid', localUuid);
        }
    };

    initUserData(); // 调用函数初始化 uuid

    // 注册方法
    async function register(username, password) {
        const hashPassword = encryptPassword(password);
        console.log(hashPassword);
        try {
            const response = await api.post('/auth/register', { username, password: hashPassword });
            Object.assign(data, response.data);
            ElMessage.success('注册成功');
        } catch (err) {
            ElMessage.error(err.response.data.message);
            return err.response.data; // 返回错误信息
        }
    }

    // 登录方法
     const login = async (username, password)=>{
        const hashPassword = encryptPassword(password);
        try {
            const response = await api.post('/auth/login', { username, password: hashPassword });
            Object.assign(data, response.data);
            ElMessage.success('登录成功');
        } catch (err) {
            ElMessage.error(err.response.data.message);
            return err.response.data; // 返回错误信息
        }
    }

    // 登出方法
    const logout = async ()=>{
        // 清空用户信息
        Object.keys(data).forEach(key => {
            data[key] = null; // 将每个属性的值设置为 null
            Cookies.remove('token') // 清除cookie
        });
        ElMessage.success('登出成功');
    }

    // 获取uuid
    const getUUID = () => {
        return localStorage.getItem('user_uuid')
    }

    // 获取用户id
    const getUserId = () => {
        return data.id || null
    }

    return {
        data,
        register,
        login,
        logout,
        getUUID,
        getUserId
    };
}, {
    persist: {
        paths: ['data']
    },
});
