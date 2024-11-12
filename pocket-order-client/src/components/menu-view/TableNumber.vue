<template>
<div class="table-number-root">
  <span class="No">桌号No.</span>
  <select ref="tableSelect" v-model="tableStore.currentTableId">
    <option v-for="(item, index) in tableStore.tables" :key="index" :value="item.id">{{item.label}}</option>
  </select>
</div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {useTableStore} from "@/stores/table.js";
import {ElMessage} from "element-plus";
const tableStore = useTableStore()
const tableSelect = ref(null)

// 监听 EventBus 事件
const selectTableHandler = () => {
  if (tableSelect.value) {
    tableSelect.value.open = true
  }
};

tableStore.EventBus.$on('select-table', selectTableHandler);

// 组件销毁时移除事件监听
onMounted(() => {
  return () => {
    tableStore.EventBus.$off('select-table', selectTableHandler);
  };
});
</script>

<style scoped lang="scss">
.table-number-root {
  display: flex;
  align-items: center;
  width: 130px;
  border: 1px solid #E3E3E3;
  padding: 2px 10px;
  border-radius: 20px;

  .No {
    color: #0022AB;
    font-weight: 600;
    white-space: nowrap;
  }

  select{
    border: none;
    outline: none;
    color: #909FBE;
    background-color: #fff;
  }
}
</style>
