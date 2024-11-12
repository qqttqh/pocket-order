<template>
<div class="menu-card-deputy-root" :class="{'expansion': expansion}">
  <div class="top-bar">
    <div class="total-checked-container">
      <el-button @click="tableStore.checkedAll" v-show="!(menuStore.selectedCount === menuStore.unSelectedCount)" circle size="small" />
      <el-button @click="tableStore.unckeckedAll" v-show="menuStore.selectedCount === menuStore.unSelectedCount" class="custom-el-button" size="small" color="#C2A379" :icon="IconCheck" circle />
      <span>已选菜品（{{menuStore.selectedTotal}}件）</span>
    </div>
    <div class="close-button-container">
      <el-button @click="tableStore.clearCount" size="small" :icon="IconDelete" round>清空菜单</el-button>
      <el-button @click="emit('close')" class="custom-close-button" :icon="IconClose" size="small" circle />
    </div>
  </div>
  <div class="main-container">
    <DishesCardOption v-for="item in menuStore.menuData" :data="item"/>
  </div>
</div>
</template>

<script setup>
import DishesCardOption from "@/components/menu-view/DishesCardOption.vue";
import {useMenuStore} from "@/stores/menu.js";
import IconClose from "@/components/icons/IconClose.vue";
import IconDelete from "@/components/icons/IconDelete.vue";
import IconCheck from "@/components/icons/IconCheck.vue";
import {useTableStore} from "@/stores/table.js";

const menuStore = useMenuStore()
const tableStore = useTableStore()

defineProps({
  expansion: Boolean,
})

const emit = defineEmits(['close'])

</script>

<style lang="scss">
.menu-card-deputy-root{
  position: absolute;
  z-index: 2;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0;
  background-color: #F4F7FC;
  border-radius: 10px 10px 0 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: height .5s ease;

  .top-bar {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 50px;
    padding: 10px 10px 0 20px;

    .total-checked-container {
      display: flex;
      justify-content: start;
      gap: 7px;
      align-items: center;

      .custom-el-button{
        margin: 0;
      }
    }

    .close-button-container {
      padding: 8px 10px;
      border-radius: 20px;

      .custom-close-button {
        padding: 0;
        margin-left: 5px;
        border: 2px solid #151515;

        .el-icon {
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 10px;
          width: 3em !important;
          height: 3em !important;

        }
      }
    }
  }
  
  .main-container{
    width: 100%;
    height: calc(100% - 50px - 65px);
    overflow: auto;
    scrollbar-width: none;
  }
}

.expansion{
  height: 400px !important;
  //max-height: 500px !important;
  overflow: auto !important;
}
</style>
