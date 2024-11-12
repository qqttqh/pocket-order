<template>
  <div class="setting-view-root">
    <div class="user-info-container">
      <UserInfoCard v-show="userStore.data.id" />
      <NotLoginCard v-show="!userStore.data.id" @openLAR="LARStatus = 1"/>
    </div>

    <div class="charts-container" v-if="userStore.data.role === 'ADMIN'">
      <DailyOrderStats/>
    </div>

    <div class="setting-option-container" v-show="userStore.data.id">
      <DishesAdmin v-if="userStore.data.role === 'ADMIN'"/>
      <GroupAdmin v-if="userStore.data.role === 'ADMIN'"/>
      <LogoutOption/>
    </div>
  </div>
  <transition name="el-fade-in-linear"><LoginCard @close="closeLAR" @switch="switchLAR" v-show="LARStatus === 1"/></transition>

  <transition name="el-fade-in-linear"><RegisterCard @close="closeLAR" @switch="switchLAR" v-show="LARStatus === 2"/></transition>


</template>

<script setup>
import UserInfoCard from "@/components/setting-view/UserInfoCard.vue";
import DishesAdmin from "@/components/setting-view/DishesAdminOption.vue";
import GroupAdmin from "@/components/setting-view/GroupAdminOption.vue";
import LogoutOption from "@/components/setting-view/LogoutOption.vue";
import NotLoginCard from "@/components/setting-view/NotLoginCard.vue";
import LoginCard from "@/components/setting-view/not-login-card/LoginCard.vue";
import {onMounted, ref} from "vue";
import RegisterCard from "@/components/setting-view/not-login-card/RegisterCard.vue";
import {useUserStore} from "@/stores/user.js";
import {useOrdersStore} from "@/stores/orders.js";
import DailyOrderStats from "@/components/setting-view/DailyOrderStats.vue";

const LARStatus = ref(0) // 0/1/2--都不显示/显示登录/显示注册
const userStore = useUserStore()
const orderStore = useOrdersStore()

function closeLAR() {LARStatus.value = 0}
function switchLAR() {LARStatus.value === 1 ? LARStatus.value = 2 : LARStatus.value = 1}

onMounted(()=>{
  // 加载统计数据
  orderStore.loadDailyOrderStatsData()
})
</script>

<style scoped lang="scss">
*{
  box-sizing: border-box;
}
.setting-view-root {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
  overflow: hidden;

  .user-info-container {
    width: 100%;
  }

  .setting-option-container {
    display: flex;
    flex-direction: column;
    gap: 15px;
    width: 100%;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 20px;

    .custom-el-divider {
      margin: 10px 0;
    }
  }
}
</style>
