<template>
  <div class="not-login-card-root" @click="emits('openLAR')">
    <div class="avatar-container">
      <el-skeleton style="--el-skeleton-circle-size: 50px" animated>
        <template #template>
          <el-skeleton-item variant="circle" />
        </template>
      </el-skeleton>
    </div>
    <div class="info-container">
      <el-skeleton :rows="1" animated />
      <div class="text"><span>登录/注册</span><el-icon color="#0022AB"><IconRight/></el-icon></div>
    </div>
  </div>
</template>

<script setup>
import IconRight from "@/components/icons/IconRight.vue";

const emits = defineEmits(["openLAR"]);
</script>

<style scoped lang="scss">
.not-login-card-root {
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

    .text{
      display: flex;
      justify-content: end;
      align-items: center;
      text-align: end;
      color: #0022AB;
      padding-top: 10px;
      font-weight: 600;
    }

  }
}
</style>
