<template>
  <div class="button-bar-root">
    <button :class="{ 'button-color': route.name === 'menu' }" @click="switchRoute('menu')">点餐</button>
    <button :class="{ 'button-color': route.name === 'order' }" @click="switchRoute('order')">订单</button>
    <button :class="{ 'button-color': route.name === 'setting' }" @click="switchRoute('setting')">设置</button>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

function switchRoute(name) {
  if (name !== route.name) {
    router.push({ name })
  }
}
</script>

<style scoped lang="scss">
.button-bar-root {
  display: flex;
  justify-content: space-around;
  align-items: start;
  width: 100%;
  height: 100%;
  background-color: transparent;

  button {
    height: 50px;
    border: none;
    border-radius: 0;
    flex-grow: 1;
    font-size: 18px;
    color: #6C6C6C;
    font-weight: 600;
    background-color: transparent;
    cursor: pointer;
    transition: color 0.3s ease;
  }

  .button-color {
    color: #0022AB;
  }
}
</style>
