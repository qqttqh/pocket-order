<template>
<div class="menu-card-root" v-show="data.count > 0">
  <div class="option-container">
    <el-button @click="tableStore.switchChecked(data.id, true)" v-show="!data.checked" circle size="small" />
    <el-button @click="tableStore.switchChecked(data.id, false)" v-show="data.checked" class="custom-el-button" size="small" color="#C2A379" :icon="IconCheck" circle />
  </div>
  <!--图片容器-->
  <div class="img-container">
    <img :src="data.img" alt="">
  </div>
  <main>
    <!--菜品名称容器-->
    <div class="dishes-name-container">
      <span>{{ data.name }}</span>
    </div>
    <!--价格和计数器容器-->
    <div class="price-and-counter-container">
    <!--价格容器-->
      <div class="price-container">
        <span style="color: #F95731">¥ {{data.price }}</span>
      </div>
      <!--计数器容器-->
      <div class="counter-container">
        <CounterCard :dishesId="data.id" :count="data.count" />
      </div>
    </div>
  </main>
</div>
</template>

<script setup>
import CounterCard from "@/components/menu-view/CounterCard.vue";
import IconCheck from "@/components/icons/IconCheck.vue";
import {useTableStore} from "@/stores/table.js";
defineProps({
  data: Object
})
const tableStore = useTableStore()

</script>

<style scoped lang="scss">
.menu-card-root {
  display: flex;
  padding: 20px;

  .option-container {
    display: flex;
    align-items: center;
    padding-right: 10px;

    .custom-el-button{
        margin: 0;

    }
  }

  .img-container{
    display: flex;
    justify-content: center;
    align-items: center;

    img {
      width: 70px;
      height: 70px;
      border-radius: 10px;
    }
  }

  main{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 100%;
    padding-left: 10px;

    .dishes-name-container{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: start;
      width: 100%;
      height: 30px;
    }

    .price-and-counter-container{
      display: flex;
      flex-grow: 1;

      .price-container {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: start;
        width: 50px;

      }

      .counter-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: end;
        padding: 0 10px;
      }
    }
  }
}
</style>
