// stores/tableStore.js
import { defineStore } from 'pinia';
import {computed, reactive, ref, watch} from 'vue';
import { useUserStore } from '@/stores/user.js';
import {useMenuStore} from "@/stores/menu.js";
import {ElMessage} from "element-plus"; // 引入用户存储

export const useTableStore = defineStore('tableStore', () => {
    const userStore = useUserStore();
    const menuStore = useMenuStore();
    const tables = ref([
        { id: 1, label: 'A001' },
        { id: 2, label: 'A002' },
        { id: 3, label: 'A003' },
        { id: 4, label: 'A004' },
        { id: 5, label: 'A005' },
        { id: 6, label: 'A006' },
        { id: 7, label: 'A007' },
        { id: 8, label: 'A008' },
        { id: 9, label: 'A009' },
        { id: 10, label: 'A010' }
    ]);
    const currentTableId = ref(null); // 初始为 null，表示未选择桌子
    const socket = ref(null);

    // 事件总线
    const EventBus = reactive({
        events: {},
        $emit(event, data) {
            if (!this.events[event]) return;
            this.events[event].forEach(callback => callback(data));
        },
        $on(event, callback) {
            if (!this.events[event]) {
                this.events[event] = [];
            }
            this.events[event].push(callback);
        },
    });

    // 创建 WebSocket 连接
    const connect = () => {

        let userId = userStore.data.id // 优先使用 id，若不存在则使用 uuid
        if (!userId){
            console.log('没有userid')
            userId = userStore.getUUID() // 使用uuid
        }
        const tableId = currentTableId.value; // 获取当前桌子 ID

        console.log('开始尝试建立连接' + userId + tableId)
        if (tableId) { // 如果 tableId 存在才建立连接
            socket.value = new WebSocket(`/api/ws/table?userId=${userId}&tableId=${tableId}`);

            socket.value.onopen = () => {
                console.log('点餐同步已建立');
            };

            socket.value.onmessage = (event) => {
                const message = JSON.parse(event.data);
                switch (message.type) {
                    case "request-dishes-data":
                        sendMessage({
                            type: "response-dishes-data",
                            target: message.requestId,
                            data: menuStore.hasCountData
                        })
                        break;
                    case "response-dishes-data":
                        menuStore.syncMenuData(message.data)
                        break;
                    case "add-count":
                        menuStore.addCount(message.id)
                        break;
                    case "reduce-count":
                        menuStore.reduceCount(message.id)
                        break;
                    case "clear-count":
                        menuStore.clearCount()
                        break;
                    case "switch-checked":
                        const id = message.data.id;
                        const checked = message.data.checked;
                        menuStore.switchChecked(id, checked)
                        break;
                    case "checked-all":
                        menuStore.checkedAll()
                        break;
                    case "unchecked-all":
                        menuStore.unCheckedAll()
                        break;
                    case "ordered":
                        menuStore.clearCount()
                        ElMessage.success('同桌已下单成功')
                        break;


                    default:
                        console.log('未知消息类型:', message.type);
                }
            };

            socket.value.onerror = (error) => {
                console.error('点餐同步发生错误:', error);
            };

            socket.value.onclose = () => {
                console.log('点餐同步已关闭');
            };
        }
    };

    // 发送消息
    const sendMessage = (message) => {
        if (socket.value && socket.value.readyState === WebSocket.OPEN) {
            socket.value.send(JSON.stringify(message));
        } else {
            console.log('点餐同步尚未连接');
        }
    };

    // 设置桌子ID
    const setTableId = (id) => {
        currentTableId.value = id
    }

    // 获取桌号
    const getTableNumber = () => {
        const table = tables.value.find(item => item.id === currentTableId.value);
        return table ? table.label : null; // 如果找到对应的 table，返回 label，否则返回 null
    }

    // 菜品计数加一
    const addCount = (id)=>{
        if (currentTableId.value){
            menuStore.addCount(id) // 本地仓库计数加一
            const message = {type: 'add-count',id: id}
            sendMessage(message) // 广播信息
        }else {
            EventBus.$emit('select-table')
            ElMessage.warning('请选择桌号')
        }

    }

    // 菜品计数减一
    const reduceCount = (id)=>{
        if(currentTableId.value){
            menuStore.reduceCount(id) // 本地仓库计数减一
            const message = {type: 'reduce-count',id: id}
            sendMessage(message) // 广播信息
        }else {
            ElMessage.warning('请选择桌号')
        }
    }

    // 清空计数
    const clearCount = ()=>{
        menuStore.clearCount()
        const message = {type: 'clear-count'}
        sendMessage(message) // 广播信息
    }

    // 切换选中
    const switchChecked = (id, checked)=>{
        menuStore.switchChecked(id, checked)
        const message = {
            type: 'switch-checked',
            data: {id, checked}
        }
        sendMessage(message)
    }

    // 全选
    const checkedAll = ()=>{
        menuStore.checkedAll()
        const message = {type: 'checked-all'}
        sendMessage(message)
    }

    // 取消全选
    const unckeckedAll = () => {
        menuStore.unCheckedAll()
        const message = {type: 'unchecked-all'}
        sendMessage(message)
    }

    // 下单
    const ordered = () => {
        menuStore.clearCount()
        const message = {type: 'ordered'};
        sendMessage(message)
    }

    // 观察 currentTableId 的变化，自动重连
    watch(currentTableId, (newTableId) => {
        if(socket.value){ // 先退出之前的桌子
            socket.value.close();
            socket.value = null;
        }
        if (newTableId) { // 有选择桌号-建立连接
            connect();
        }
    });

    return {
        EventBus,
        tables,
        currentTableId,
        ordered,
        setTableId,
        getTableNumber,
        addCount,
        reduceCount,
        clearCount,
        switchChecked,
        checkedAll,
        unckeckedAll,
    };
});
