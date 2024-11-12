<template>
<div class="menu-card-root" :id="`dishes${data.id}`">
  <!--图片容器-->
  <div class="img-container">
    <img :src="data.img" alt="">
  </div>
  <main>
    <!--菜品名称容器-->
    <div class="dishes-name-container">
      <span>{{ data.name }}</span>
    </div>
    <div class="introduce-container">
      {{ data.introduce }}
    </div>
    <!--价格和计数器容器-->
    <div class="price-and-counter-container">
    <!--价格容器-->
      <div class="price-container">
        <span style="color: #F95731">¥ {{data.price }}</span>
      </div>
      <!--计数器容器-->
      <div class="counter-container">
        <CounterCard :dishesId="data['id']" :count="data['count']" />
      </div>
    </div>
  </main>
</div>
</template>

<script setup>
import CounterCard from "@/components/menu-view/CounterCard.vue";

defineProps(['data'])
</script>

<style scoped lang="scss">
.menu-card-root {
  display: flex;
  padding: 8px;

  .img-container{
    display: flex;
    justify-content: center;
    align-items: center;

    img {
      width: 100px;
      height: 100px;
      border-radius: 10px;
    }
  }

  main{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 100%;
    padding-left: 10px;
    overflow: hidden;

    .introduce-container {
      width: 55%;
      height: 40px;
      font-size: 10px;
      color: #9f9f9f;
      line-height: 1.8;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: auto;
      text-overflow: ellipsis;
      scrollbar-width: none;
    }

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
        justify-content: end;
        align-items: start;
        white-space: nowrap;
      }

      .counter-container {
        flex-grow: 1;
        display: flex;
        //flex-direction: column;
        justify-content: end;
        align-items: end;
        padding: 0 10px;
      }
    }
  }
}
</style>
