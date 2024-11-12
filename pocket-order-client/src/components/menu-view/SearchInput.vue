<template>
  <el-input v-model="menuStore.searchKeyword" :prefix-icon="IconSearch" placeholder="搜索" class="custom-input" clearable/>
</template>

<script setup>
import {ref, watch} from 'vue'
import IconSearch from "@/components/icons/IconSearch.vue";
import {useMenuStore} from "@/stores/menu.js";

const input = ref('')
const menuStore = useMenuStore()

watch(input, (newVal) => {
  menuStore.searchByName(newVal)
})
</script>

<style lang="scss">
.custom-input {
  //width: 210px;
  flex-grow: 1;

  font-size: 16px;
  .el-input__wrapper {
    border-radius: 20px !important; /* 设置圆角边框 */
  }
}
</style>
