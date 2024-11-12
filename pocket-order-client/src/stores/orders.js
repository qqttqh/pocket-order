import {ref, computed, watch} from 'vue'
import { defineStore } from 'pinia'
import {useUserStore} from "@/stores/user.js";
import {useMenuStore} from "@/stores/menu.js";
import {useTableStore} from "@/stores/table.js";
import api from "@/api/api.js";
import {ElMessage} from "element-plus";
import {pushMessage} from "@/utils/barkUtil.js";

export const useOrdersStore = defineStore('ordersStore', () => {
    const orderData = ref([]) // 订单列表
    const sortedOrders  = computed(()=>{
        return orderData.value.slice().sort((a, b) => b.id - a.id);
    })
    const newOrderCache = ref([]) // 新订单缓存
    const totalPages = ref(99999999)
    const DailyOrderStatsData = ref([]) // 每日订单统计数据
    const dateList = computed(() => DailyOrderStatsData.value.map((item) => Number(item.date.slice(-2)))) //日期列表
    const totalOrdersList = computed(() => DailyOrderStatsData.value.map((item) => item.totalOrders)) // 每日总订单列表
    const totalSalesList = computed(() => DailyOrderStatsData.value.map((item) => item.totalSales)) // 每日总销售额列表

    const userStore = useUserStore()
    const menuStore = useMenuStore()
    const tableStore = useTableStore()
    const socket = ref(null);
    const reconnectInterval = ref(null); // 重连定时器

    // 加载统计数据
    const loadDailyOrderStatsData = ()=>{
        if (userStore.data.role === 'ADMIN'){
            api.get('/admin/daily-order-stats')
                .then((response)=>{
                    DailyOrderStatsData.value = response.data.data;
                })
        }
    }

    // 创建 WebSocket 连接
    const connect = () => {

        let userId = userStore.data.id // 优先使用 id，若不存在则使用 uuid
        if (!userId){
            userId = userStore.getUUID()  // 使用uuid
        }

        let wsUrl = ''
        if(userStore.data.role === 'ADMIN'){
            wsUrl = `/api/ws/orders?userId=${userId}&role=ADMIN`
        }else{
            wsUrl = `/api/ws/orders?userId=${userId}`
        }

        if (userId) {
            socket.value = new WebSocket(wsUrl);

            socket.value.onopen = () => {
                console.log('订单同步已建立');
                clearInterval(reconnectInterval.value); // 连接成功，清除定时器
            };

            socket.value.onmessage = (event) => {
                const message = JSON.parse(event.data);
                switch (message.type) {
                    case "update-order-progress": // 更新订单进度
                        // 找出订单
                        const order = orderData.value.find(item => item.id === message.orderId);
                        if(order){
                            // 找出菜品
                            const item = order.order_list.find(item => item.id === message.itemId);
                            if (item) {
                                item.success = message.newValue;
                            }
                        }
                        break;
                    case "mark-order-as-success": // 标记订单完成
                        // 找出订单
                        const successOrder = orderData.value.find(item => item.id === message.orderId);
                        if(successOrder){
                            successOrder.status = 1
                        }
                        break;
                    case "create-order": // 用户创建订单
                        newOrderCache.value.push(message.data)
                        ElMessage.success('有新的订单')
                        break;
                    case "cancel-order": // 删除用户订单
                        const index = orderData.value.findIndex(item => item.id === message.orderId); // 查找订单
                        if(index !== -1){orderData.value.splice(index, 1)} // 删除本地仓库订单
                        if(message.userId === userStore.data.id){
                            userStore.data.integrate -= Math.floor(message.totalPrice);
                        }
                        ElMessage.success('订单已取消')
                        break;
                    default:
                        console.log('未知消息类型:', message.type);
                }
            };

            socket.value.onerror = (error) => {
                console.error('订单同步发生错误:', error);
            };

            socket.value.onclose = () => {
                console.log('订单同步已关闭');
                startReconnect(); // 连接关闭后尝试重新连接
            };
        }
    };

    // 发送消息
    const sendMessage = (message) => {
        if (socket.value && socket.value.readyState === WebSocket.OPEN) {
            socket.value.send(JSON.stringify(message));
        } else {
            console.log('WebSocket尚未连接');
        }
    };

    // 建立socket连接
    connect()

    // 开始自动重连
    const startReconnect = () => {
        if (!reconnectInterval.value) {
            reconnectInterval.value = setInterval(() => {
                console.log('尝试重新连接...');
                connect();
            }, 5000); // 每5秒尝试重连
        }
    };

    // 监听 WebSocket 状态变化
    watch(socket, (newSocket) => {
        if (!newSocket) {
            console.log('实时订单失去同步，开始重连...');
            startReconnect();
        }
    });

    // 监听用户切换
    const userIdComputed = computed(()=>{
        return userStore.data.id
    })
    watch(userIdComputed, (newValue) => {
        socket.value.close()
        connect()
    })

    // 更新订单进度
    const updateOrderProgress = (targetId, orderId, itemId, newValue, orderList)=>{
        const message = {
            type: 'update-order-progress',
            targetId,
            orderId,
            itemId,
            newValue
        }
        sendMessage(message)
        api.put('/admin/orders/update-order-list',{
            id: orderId,
            order_list: orderList
        })
    }

    // 标记订单完成
    const markOrderAsSuccess = (targetId, orderId)=>{
        const message= {
            type: 'mark-order-as-success',
            targetId,
            orderId
        }
        sendMessage(message)
        api.get(`/admin/orders/mark-order-as-success?orderId=${orderId}`)
            .then(()=>{
                // 找出订单
                const order = orderData.value.find(item => item.id === orderId);
                if(order){
                    // 标记完成
                    order.status = 1
                }
                ElMessage.success('订单完成')
            })
    }

    // 创建订单
    const createOrder = ()=>{
        if(tableStore.currentTableId){
            if(menuStore.order_list.length > 0){
                const userUuid = userStore.data.id ? null : userStore.getUUID(); // 若有user_id,不使用uuid
                const data = {
                    user_id: userStore.data.id ? userStore.data.id : null,
                    user_uuid: userUuid,
                    table_number: tableStore.getTableNumber(),
                    order_list: menuStore.order_list,
                    total_price: menuStore.selectedTotalPrice,
                }
                api.post('/orders/create-order', data)
                    .then((response)=>{
                        orderData.value.unshift(response.data.data) // 创建成功将订单推入队列
                        tableStore.ordered() // 通知同桌清空菜单
                        userStore.data.integrate += Math.floor(response.data.data.total_price)
                        const message = {
                            type: 'create-order',
                            data: response.data.data
                        }
                        sendMessage(message)
                        ElMessage.success(response.data.message)
                        pushMessage(response.data.data.order_list, response.data.data.total_price) // 推送下单通知到手机
                    })
                    .catch((err)=>{
                        ElMessage.error(err.response.data.message)
                    })
            }else {
                ElMessage.warning('请选择菜品')
            }
        }else{
            ElMessage.warning('请选择桌号')
        }

        }

    // 取消用户订单
    const cancelOrder = (orderId, userId, userUuid, totalPrice)=>{
        api.delete(`/admin/cancel-order?orderId=${orderId}`)
            .then((response)=>{
                console.log(response)
                ElMessage.success(response.data)
                const targetId = userId ? userId : userUuid
                const message = {
                    type: 'cancel-order',
                    targetId: targetId,
                    orderId: orderId,
                    userId: userId,
                    userUuid: userUuid,
                    totalPrice: totalPrice
                }
                sendMessage(message)
                const index = orderData.value.findIndex(item => item.id === orderId); // 查找订单
                if(index !== -1){orderData.value.splice(index, 1)} // 删除本地仓库订单
                if(userId === userStore.data.id){ // 较少积分
                    userStore.data.integrate -= Math.floor(totalPrice);
                }
            })
            .catch((err)=>{
                ElMessage.error(err.response.data)
            })
    }

// 处理新订单缓存
    const handleNewOrderCache = () => {
        while (newOrderCache.value.length > 0) {
            const order = newOrderCache.value.shift();

            // 判断 orderData 中是否已经存在相同 id 的订单
            const exists = orderData.value.some(existingOrder => existingOrder.id === order.id);

            // 如果不存在，则推入 orderData
            if (!exists) {
                orderData.value.unshift(order);
            }
        }
    };

    // 获取订单
    const getOrder = (page)=>{
        if(userStore.data.role === 'ADMIN' && page <= totalPages.value){ // 管理员获取订单
            api.get(`/admin/orders?page=${page}`)
            .then((response)=>{
                totalPages.value = response.data.totalPages // 保存总页数
                const orders = response.data.content // 获取订单列表
                if (orders.length > 0) {
                    orders.forEach(order => {
                        const isExist = orderData.value.some(item => item.id === order.id);
                        if (!isExist) {
                            orderData.value.push(order);
                        }
                    })
                }
            })
        }else if(page <= totalPages.value){ // 普通用户获取订单列表
            api.get(`/orders?page=${page}`)
                .then((response)=>{
                    totalPages.value = response.data.totalPages // 保存总页数
                    const orders = response.data.content // 获取订单列表
                    if (orders.length > 0) {
                        orders.forEach(order => {
                            const isExist = orderData.value.some(item => item.id === order.id);
                            if (!isExist) {
                                orderData.value.push(order);
                            }
                        })
                    }
                })
        }
    }

    // 清空订单
    const clearOrder = ()=>{
        orderData.value = []
    }

    return {
        orderData,
        sortedOrders,
        newOrderCache,
        totalPages,
        dateList,
        totalOrdersList,
        totalSalesList,
        loadDailyOrderStatsData,
        createOrder,
        cancelOrder,
        getOrder,
        updateOrderProgress,
        markOrderAsSuccess,
        clearOrder,
        handleNewOrderCache
    }
})
