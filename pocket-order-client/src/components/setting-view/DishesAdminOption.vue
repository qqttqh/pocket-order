<template>
  <el-button @click="isShow = true" :icon="IconOrder" class="custom-el-button" size="large">菜品管理</el-button>
  <transition name="el-fade-in-linear">
    <div class="dishes-admin-page" v-show="isShow">
      <div class="top-bar">
        <el-button @click="isShow = false" class="custom-el-button--" circle :icon="IconLeft" size="large"></el-button>
        <div class="title-div"><span>菜品管理</span></div>
        <AddDishesBtn/>
      </div>
      <main>
        <GroupOption v-for="item in menuStore.menuGroup" :data="item"/>
      </main>
    </div>
  </transition>
</template>


<script setup>
import IconOrder from "@/components/icons/IconOrder.vue";
import IconLeft from "@/components/icons/IconLeft.vue";
import {useMenuStore} from "@/stores/menu.js";
import GroupOption from "@/components/setting-view/dishes-admin-option/GroupOption.vue";
import {ref} from "vue";
import AddDishesBtn from "@/components/setting-view/dishes-admin-option/AddDishesBtn.vue";
import DishesAdminCard from "@/components/setting-view/dishes-admin-option/DishesAdminCard.vue";

const isShow = ref(false)
const menuStore = useMenuStore()

</script>

<style scoped lang="scss">
*{
  box-sizing: border-box;
}
  .custom-el-button{
    display: flex;
    justify-content: start;
    width: 100%;
    border-radius: 15px;
    margin: 0;
  }
  .dishes-admin-page {
    position: fixed;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    z-index: 2;
    //display: flex;
    //flex-direction: column;
    width: 100vw;
    height: 100vh;
    max-width: 430px;
    min-width: 375px;
    background-color: #fff;
    overflow: auto;
    scrollbar-width: none;

    .top-bar {
      position: sticky;
      top: 0;
      left: 0;
      z-index: 3;
      display: flex;
      justify-content: start;
      align-items: center;
      width: 100%;
      padding: 10px 20px;
      color: #6C6C6C;
      background-color: #F4F7FC;

      .title-div {
        flex-grow: 1;
        display: flex;
        justify-content: center;
        font-size: 18px;
        font-weight: 600;

        > .custom-el-button-- {
          border: none !important;
        }

        > span {
          //transform: translateX(-20px);
          color: #0022AB;
        }
      }
    }

    > main {
      display: flex;
      flex-direction: column;
      gap: 40px;
      overflow: auto;
      padding: 20px 10px;


    }
  }
</style>
