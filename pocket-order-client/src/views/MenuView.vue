<template>
<div class="menu-view-root">
  <MenuCard/>
  <!--顶部栏-->
  <div class="top-bar">
    <TableNumber/>
    <SearchInput/>
  </div>

  <div class="menu-container">
    <aside>
      <AnchorCard :searchAnchor="menuStore.searchAnchor" :menuGroup="menuStore.menuGroup" :container="mainContainer"/>
    </aside>
    <main ref="mainContainer" class="dishes-scroll-container">
      <GroupCard v-for="item in menuStore.menuGroup" :key="item.id" :groupId="item.id" />
    </main>
  </div>
</div>
</template>

<script setup>
import {useMenuStore} from "@/stores/menu.js";
import TableNumber from "@/components/menu-view/TableNumber.vue";
import SearchInput from "@/components/menu-view/SearchInput.vue";
import GroupCard from "@/components/menu-view/GroupCard.vue";
import AnchorCard from "@/components/menu-view/AnchorCard.vue";
import {onMounted, ref} from "vue";
import MenuCard from "@/components/menu-view/MenuCard.vue";
import { useRoute } from 'vue-router';
import {useTableStore} from "@/stores/table.js";

const menuStore = useMenuStore()
const tableStore = useTableStore()
const mainContainer = ref(null)

// 使用 useRoute 钩子获取 URL 参数
const route = useRoute();

onMounted(()=>{
  if(route.query.tableId){
    const tableId = Number(route.query.tableId); // 将 tableId 转换为数字
    tableStore.setTableId(tableId)
  }
})
</script>
<style scoped lang="scss">
@import url('../assets/scss/menu-view.scss');
</style>
