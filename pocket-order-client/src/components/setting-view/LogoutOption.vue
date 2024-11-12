<template>
  <el-button @click="logout" :icon="IconShutDown" class="custom-el-button" size="large">退出登录</el-button>
</template>

<script setup>
import IconShutDown from "@/components/icons/IconShutDown.vue";
import {useUserStore} from "@/stores/user.js";
import {useOrdersStore} from "@/stores/orders.js";
const userStore = useUserStore()
const orderStore = useOrdersStore()

const logout = ()=>{
  userStore.logout()
      .then(()=>{
        orderStore.clearOrder()
      })
}
</script>

<style scoped lang="scss">
  .custom-el-button{
    display: flex;
    justify-content: start;
    width: 100%;
    border-radius: 15px;
    margin: 0;
  }
</style>
