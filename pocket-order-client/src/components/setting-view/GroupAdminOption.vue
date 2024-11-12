<template>
  <el-button @click="isShow = true" :icon="IconGroup" class="custom-el-button" size="large">分组管理</el-button>
  <transition name="el-fade-in-linear">
    <div class="group-admin-page" v-show="isShow">
      <div class="top-bar">
        <el-button @click="isShow = false" class="custom-el-button--" circle :icon="IconLeft" size="large"></el-button>
        <div class="title-div"><span>分组管理</span></div>
        <AddGroupCard/>
      </div>
      <main>
        <GroupAdminCard v-for="item in menuStore.menuGroup" :data="item"/>
      </main>
    </div>
  </transition>
</template>


<script setup>
import IconLeft from "@/components/icons/IconLeft.vue";
import {ref} from "vue";
import {useMenuStore} from "@/stores/menu.js";
import IconGroup from "@/components/icons/IconGroup.vue";
import GroupAdminCard from "@/components/setting-view/group-admin-option/GroupAdminCard.vue";
import AddGroupCard from "@/components/setting-view/group-admin-option/AddGroupCard.vue";

const isShow = ref(false)
const menuStore = useMenuStore()

</script>

<style scoped lang="scss">
.custom-el-button{
  display: flex;
  justify-content: start;
  width: 100%;
  border-radius: 15px;
  margin: 0;
}
.group-admin-page {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2;
  display: flex;
  flex-direction: column;
  width: 100vw;
  height: 100vh;
  max-width: 430px;
  min-width: 375px;
  background-color: #F4F7FC;

  .top-bar {
    display: flex;
    justify-content: start;
    align-items: center;
    width: 100%;
    padding: 10px 20px;
    color: #6C6C6C;

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
        transform: translateX(-20px);
        color: #0022AB;
      }
    }
  }

  > main {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    gap: 10px;
    overflow: auto;
    padding: 20px 20px;

  }
}
</style>
