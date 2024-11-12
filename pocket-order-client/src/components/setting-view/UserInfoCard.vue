<template>
  <div class="user-info-card-root">
    <div class="avatar-container">
      <el-avatar :size="50" :src="userStore.data.avatar" />
    </div>
    <div class="info-container">
      <h3>{{ userStore.data.username }}</h3>
      <div class="id-and-integral-container">
        <span>ID: {{userStore.data.id}}</span>
        <span>积分: {{userStore.data.integrate}}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useUserStore} from "@/stores/user.js";

const userStore = useUserStore()
</script>

<style scoped lang="scss">
.user-info-card-root {
  display: flex;
  background-color: #fff;
  border-radius: 20px;
  width: 100%;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);


  .info-container{
    flex-grow: 1;
    padding-left: 20px;
    color: #6C6C6C;

    h3{
      color: #0022AB;
    }

    .id-and-integral-container{
      display: flex;
      justify-content: space-between;
      padding: 10px 0;
    }
  }
}
</style>
