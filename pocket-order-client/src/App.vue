<template>
<div class="root-container">
  <main>
    <RouterView/>
  </main>
  <div class="bottom-bar">
    <ButtonBar/>
  </div>
</div>
</template>

<script setup>
import ButtonBar from "@/components/BottomBar.vue";

//引入echarts
import * as echarts from "echarts";
import {provide} from "vue";
//通过provide提供echarts
provide("echarts", echarts);

</script>

<style scoped lang="scss">
@import url('@/assets/scss/app.scss');
</style>
