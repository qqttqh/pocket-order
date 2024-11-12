import {ref, computed} from 'vue';
import { defineStore } from 'pinia';
import { getMenuData } from '@/api/menu.js';
import api from "@/api/api.js";
import {ElMessage} from "element-plus";

export const useMenuStore = defineStore('menuStore', () => {
    const menuData = ref([]); // 菜单数据
    const menuGroup = ref([]); // 分组数据
    const searchKeyword = ref('') // 搜索关键词
    const selectedCount = computed(()=>{
        return menuData.value.filter(item => item.checked).length
    })// 已选数目，并选中的菜品数
    const unSelectedCount = computed(()=>{
        return menuData.value.filter(item => item.count > 0).length
    })// 已选数目，未选中的菜品数
    const selectedTotal = computed(() => {
        return menuData.value
            .filter(item => item.checked)  // 筛选出 checked 为 true 的项目
            .reduce((total, item) => total + Number(item.count || 0), 0);  // 累加 count，确保是数字
    })// 已选总数
    const selectedTotalPrice = computed(()=>{
        return menuData.value
            .filter(item => item.checked)
            .reduce((total, item) => total + item.count * item.price, 0).toFixed(2)
    }) // 已选总价
    const hasCountData = computed(() => {
        return menuData.value
            .filter(item => item.count > 0) // 筛选出 count > 0 的对象
            .map(item => ({
                id: item.id,       // 保留 id
                count: item.count, // 保留 count
                checked: item.checked // 保留 checked
            }));
    }) // 已点菜品信息
    const searchResult = computed(() => {
        const keyword = searchKeyword.value;
        return menuData.value.filter(item => item.name.includes(keyword));
    }) // 搜索结果
    const order_list = computed(() => {
        return menuData.value
            .filter(item => item.count > 0 && item.checked) // 筛选出 count > 0 且 checked 为 true 的对象
            .map(item => ({
                id: item.id,      // 保留 id
                name: item.name,  // 保留 name
                price: item.price, // 保留 price
                count: item.count, // 保留 count
                success: 0        // 添加 success 字段
            }))
    }) // 订单

    // 异步加载数据
    async function loadData() {
        try {
            const resData = await getMenuData()
            menuGroup.value = resData['menuGroup']
            if(menuData.value.length > 0) {
                resData['menuData'].forEach(newItem => {
                    const existingItem = menuData.value.find(item => item['id'] === newItem['id']);

                    if (existingItem) {
                        // 如果已存在，更新原数组中的对象
                        Object.assign(existingItem, newItem);
                    } else {
                        // 如果不存在，添加新对象并初始化 count 和 checked
                        menuData.value.push({
                            ...newItem,
                            count: 0,
                            checked: false
                        });
                    }
                });
            }else {
                resData['menuData'].forEach( item => {
                    menuData.value.push({
                        ...item,
                        count: 0,
                        checked: false
                    })
                })
            }
        } catch (error) {
            console.error('无法加载菜单数据:', error)
        }
    }

    loadData()

    // 切换选中
    function switchChecked(id, boolean) {
        const itemToUpdate = menuData.value.find(item => item['id'] === id);
        if (itemToUpdate && itemToUpdate.checked !== boolean) {
            itemToUpdate.checked = boolean;
        }
    }

    // 增加count
    function addCount(id) {
        const itemToUpdate = menuData.value.find(item => item['id'] === id)
        if (itemToUpdate) {
            itemToUpdate.count += 1
            if (!itemToUpdate.checked){
                itemToUpdate.checked = true
            }
        }
    }

    // 增加count
    function reduceCount(id) {
        const itemToUpdate = menuData.value.find(item => item['id'] === id)
        if (itemToUpdate && itemToUpdate.count > 0) {
            itemToUpdate.count -= 1
            itemToUpdate.checked = itemToUpdate.count > 0
        }
    }

    // 取消全选
    function unCheckedAll() {
        menuData.value.forEach(item => {
            if (item.checked) {
                item.checked = false;
            }
        })
    }

    // 全选
    function checkedAll() {
        menuData.value.forEach(item => {
            if (item.count > 0) {
                item.checked = true;
            }
        });
    }

    // 清空计数
    function clearCount() {
        menuData.value.forEach(item => {
            item.checked = false;
            item.count = 0
        })
    }

    // 通过菜名搜索
    const searchAnchor = ref('') // 搜索锚点
    function searchByName(searchString) {
        const foundItem  = menuData.value.find(item => item.name.includes(searchString))
        if (foundItem) {
            searchAnchor.value = `#dishes${foundItem.id}`
        }
    }

    // 更新菜品信息
    async function updateDishesInfo(data){
        await api.post('/admin/menu/update', data) // 请求更新菜品信息
            .then((response) => {
                // 通过 id 查找并更新对象
                const index = menuData.value.findIndex(dish => dish.id === data.id)

                if (index !== -1) {
                    menuData.value[index] = data
                } else {
                    console.log('更新菜品信息失败');
                }
                ElMessage.success(response.data.message)
            })
            .catch((error) => {
                ElMessage.error(error.response.data.message)
            })

    }

    // 添加新菜品
    async function addDishes(data) {
        await api.post('/admin/menu/add', data) // 请求添加菜品
            .then((response) => {
                menuData.value.push(response.data.data) // 向仓库添加菜品
                ElMessage.success(response.data.message)
            })
            .catch((err)=>{
                ElMessage.error(err.response.data.message)
            })
    }

    // 删除菜品
    async function deleteDishes(id) {
        await api.post('/admin/menu/remove', {id}) // 请求删除菜品
            .then((response)=>{
                const index = menuData.value.findIndex(item => item.id === id);
                if (index !== -1) {
                    menuData.value.splice(index, 1); // 删除仓库中的菜品
                    ElMessage.success(response.data.message)
                }
            })
            .catch((err)=>{
                ElMessage.error(err.response.data.message)
            })
    }

    // 更新分组信息
    async function updateGroupInfo(data) {
        await api.post('/admin/menu-group/update', data)
            .then((response) => {
                // 通过 id 查找并更新对象
                const index = menuGroup.value.findIndex(item => item.id === data.id)

                if (index !== -1) {
                    menuGroup.value[index] = data // 更新仓库中分组信息
                } else {
                    console.log('更新分组信息失败')
                }
                ElMessage.success(response.data.message)
            })
    }

    // 添加新分组
    async function addGroup(name) {
        await api.post('/admin/menu-group/add', {name})
            .then((response) => {
                menuGroup.value.push(response.data.data) // 向仓库添加分组
                ElMessage.success(response.data.message)
            })
            .catch((err)=>{
                ElMessage.error(err.response.data.message)
            })

    }

    // 删除分组
    async function deleteGroup(id) {
        await api.post('/admin/menu-group/remove', {id})
            .then((response)=>{
                const index = menuGroup.value.findIndex(item => item.id === id)
                if (index !== -1) {
                    menuGroup.value.splice(index, 1) // 删除仓库中的分组
                }
                ElMessage.success(response.data.message)
            })
            .catch((err)=>{
                ElMessage.error(err.response.data.message)
            })
    }


    // 同步菜单数据
    function syncMenuData(newData) {
        unCheckedAll() // 先清空本地数据
        clearCount()
        // 遍历菜单数据
        menuData.value.forEach(item => {
            // 在 newData 中查找对应的项
            const newItem = newData.find(selected => selected.id === item.id);
            if (newItem) {
                // 更新 count 和 checked 属性
                item.count = newItem.count;
                item.checked = newItem.checked;
            } else {
                // 如果没有找到对应项，可以选择设置默认值
                item.count = 0; // 或其他默认值
                item.checked = false; // 或其他默认值
            }
        });
    }

    return {
        menuData,
        menuGroup,
        selectedCount,
        unSelectedCount,
        selectedTotal,
        selectedTotalPrice,
        searchAnchor,
        hasCountData,
        searchKeyword,
        searchResult,
        order_list,
        addCount,
        reduceCount,
        switchChecked,
        unCheckedAll,
        checkedAll,
        clearCount,
        searchByName,
        updateDishesInfo,
        addDishes,
        deleteDishes,
        updateGroupInfo,
        addGroup,
        deleteGroup,
        syncMenuData

    }
}, {
    persist: {
        paths: ['menuData', 'menuGroup']
    }
})
