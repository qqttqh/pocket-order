<template>
<div class="order-root">
  <!--订单号容器-->
  <div class="order-number-container">
    <span>订单号：{{ order.id }}</span>
    <span class="not-paid" v-show="order.status === 0">未支付</span>
    <span class="paid" v-show="order.status === 1">已支付</span>
  </div>
  <el-divider class="custom-el-divider" border-style="dashed" />
  <!--菜品信息容器-->
  <div class="dishes-container">
    <DishesStatusUser @updateProgress="updateProgress" v-for="item in order.order_list" :order="order" :orderItem="item"/>
  </div>
  <div class="total-price-container">
    <span>总计：</span>
    <span>¥ {{ order.total_price }}</span>
  </div>
  <el-divider class="custom-el-divider" border-style="dashed" />
  <div class="progress-bar-container">
    <el-progress class="custom-el-progress" color="#409EFF" striped-flow :percentage="progress" striped :duration="10" />
  </div>
  <div class="table-number-container">
    <div class="table-number-container">
      桌号: <span>{{ order.table_number }}</span>
    </div>

    <div class="create-time-container">
     <span>{{ order.create_time.replace("T", " ").split(".")[0] }}</span>
    </div>
  </div>
  <div class="button-container" v-if="userStore.data.role === 'ADMIN'">
    <el-popconfirm
        confirm-button-text="确认"
        cancel-button-text="取消"
        icon-color="#626AEF"
        title="确定取消订单吗?"
        @confirm="orderStore.cancelOrder(order.id, order.user_id, order.user_uuid, order.total_price)"
    >
      <template #reference>
        <span v-if="order.status === 0">取消订单</span>
      </template>
    </el-popconfirm>
    <el-button v-if="order.status === 0" color="#0022AB" @click="markOrderAsSuccess" >确认买单</el-button>
  </div>
</div>
</template>

<script setup>
import DishesStatusUser from "@/components/orders-view/DishesStatusUser.vue";
import {useUserStore} from "@/stores/user.js";
import {computed, defineProps, ref} from "vue";
import {useOrdersStore} from "@/stores/orders.js";
import {ElMessage} from "element-plus";
const userStore = useUserStore()
const orderStore = useOrdersStore()

const props = defineProps({
  order: Object
})
// 下单人id
const targetId = props.order.user_id || props.order.user_uuid
// 订单id
const orderId = props.order.id;

// 菜品列表
const orderList = ref([])
orderList.value = props.order.order_list

// 计算完成进度
const progress = computed(() => {
  const totalCount = orderList.value.reduce((acc, item) => acc + item.count, 0);
  const totalSuccess = orderList.value.reduce((acc, item) => acc + item.success, 0);

  if (totalCount === 0) return 0; // 防止除以 0 的情况

  return Math.floor((totalSuccess / totalCount) * 100); // 返回整数进度
});

// 更新进度
const updateProgress = (itemId, newValue)=>{
  const item = orderList.value.find(item => item.id === itemId); // 根据 id 查找对象
  if (item) {
    item.success = newValue; // 更新 success 值
  }
  orderStore.updateOrderProgress(targetId, orderId, itemId, newValue, orderList.value)
}

// 标记订单为完成
const markOrderAsSuccess = ()=>{
  if(progress.value === 100){
    orderStore.markOrderAsSuccess(targetId, orderId)
  }else{
    ElMessage.warning('菜未上完')
  }
}

</script>

<style lang="scss">
.order-root {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;

  .order-number-container{
    display: flex;
    justify-content: space-between;
    color: #6C6C6C;

    .not-paid{
      color: #d64e35;
    }
    
    .paid{
      color: #50a924;
    }
  }

  .total-price-container{
    display: flex;
    justify-content: end;
    padding-top: 10px;

    >span:first-child{
      font-size: 18px;
      color: #6C6C6C;
    }

    >span:nth-child(2){
      color: #F95731;
      font-size: 18px;
    }
  }

  .progress-bar-container{
    padding: 10px 0;

    .custom-el-progress {
      .el-progress__text{
        min-width: auto;
      }
    }
  }

  .custom-el-divider{
    margin: 15px 0;
  }

  .button-container{
    display: flex;
    justify-content: end;
    align-items: center;
    padding-top: 10px;

    >span {
      color: #6C6C6C;
      font-size: 14px;
      padding-right: 20px;
      text-decoration: underline;
      cursor: pointer;
    }
  }

  .table-number-container{
    display: flex;
    justify-content: space-between;
    align-items: center;

    .table-number-container{
      padding-top: 5px;
      color: #0022AB;
      //font-weight: 600;
      > span{
        color: #6C6C6C;
      }
    }

    .create-time-container{
      color: #6c6c6c;
      font-size: 14px;
    }

  }
}
</style>
