<template>
  <div class="group-option-root">
    <div class="label-container">
      <span>{{data.name}}</span>
    </div>
    <div class="dishes-admin-card-container">
      <DishesAdminCard v-for="item in menuStore.menuData" :key="item.id" :thisGroupId="data.id" :data="item" />
    </div>
  </div>
</template>

<script setup>
import DishesAdminCard from "@/components/setting-view/dishes-admin-option/DishesAdminCard.vue";
import {useMenuStore} from "@/stores/menu.js";

const props = defineProps({
  data: Object
})
const menuStore = useMenuStore()
</script>

<style scoped lang="scss">
.group-option-root {
  background-color: transparent;
  border-radius: 20px;
  padding: 10px;
  //box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

  .label-container {
    display: flex;
    padding: 0 0 20px;

    > span {
      flex-grow: 1;
      padding:  5px 15px;
      background-color: #0022AB;
      box-shadow: 0 0 10px rgba(0, 0, 0, .1);
      border-radius: 20px;
      text-align: center;
      font-size: 20px;
      font-weight: 600;
      color: white;
    }
  }

  .dishes-admin-card-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

}
</style>
