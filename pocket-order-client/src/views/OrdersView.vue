<template>
<div class="orders-view-root" v-infinite-scroll="loadOrders" :infinite-scroll-distance="100">
  <span v-if="orderStore.orderData.length < 1" class="not-order">还没有订单.....</span>

  <div v-show="cacheLength > 0"  class="handle-new-order-cache-btn-container">
    <el-badge :value="cacheLength" class="item" :offset="[-40, -3]">
      <el-button @click="orderStore.handleNewOrderCache()" color="#0022AB" class="handle-new-order-cache-btn" :icon="IconRefresh" circle size="large" />
    </el-badge>
  </div>

  <Order v-for="item in orderStore.sortedOrders" :key="item.id" :order="item" />

  <el-backtop class="custom-el-backtop" target=".orders-view-root" :right="100" :bottom="100" />
</div>
</template>

<script setup>
import Order from "@/components/orders-view/Order.vue"
import {useOrdersStore} from "@/stores/orders.js";
import IconRefresh from "@/components/icons/IconRefresh.vue";
import {computed} from "vue";

const orderStore = useOrdersStore()

const cacheLength = computed(()=>{
  return orderStore.newOrderCache.length
})
let page = 1

function loadOrders() {
  if(page <= orderStore.totalPages){
    orderStore.getOrder(page)
    page++
  }
}

</script>

<style scoped lang="scss">
.orders-view-root {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow: auto;
  scrollbar-width: none;

  .not-order {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    color: #6C6C6C;
  }

  .handle-new-order-cache-btn-container{
    position: fixed;
    right: 5%;
    bottom: 25%;
    z-index: 3;
  }

  .custom-el-backtop{
    //position: absolute !important;
    right: 5% !important;
    bottom: 15% !important;
  }
}
</style>
