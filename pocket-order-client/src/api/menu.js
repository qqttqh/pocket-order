import api from './api';

// 获取菜单数据
export function getMenuData() {
    return api.get('/menu/')
        .then((response) => {
            return response.data; // 返回数据
        })
        .catch((error) => {
            throw error; // 抛出错误
        });
}
