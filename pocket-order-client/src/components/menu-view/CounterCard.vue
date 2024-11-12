<template>
  <div class="counter-card-container">
    <el-button @click="reduce" v-show="isShow" type="primary" :icon="IconReduce" color="#0022AB" size="small" circle />
    <span v-show="isShow" >{{ count }}</span>
    <el-button @click="add" type="primary" :icon="IconAdd" color="#0022AB" size="small" circle  />
  </div>
</template>

<script setup>
import IconAdd from "@/components/icons/IconAdd.vue";
import IconReduce from "@/components/icons/IconReduce.vue";
import {ref, defineProps, computed, watch} from "vue";
import {useMenuStore} from "@/stores/menu.js";
import {useTableStore} from "@/stores/table.js";

const isShow = ref(false)
const menuStore = useMenuStore()
const tableStore = useTableStore()

const props = defineProps({
  dishesId: Number,
  count: Number
})

if(props.count){
  isShow.value = true
}

const reduce = () => {
  tableStore.reduceCount(props.dishesId)

  isShow.value = true
}
const add = () => {
  tableStore.addCount(props.dishesId)
  if(tableStore.currentTableId){
    isShow.value = true
  }
}

const propsCount = computed(() => props.count) // 包装count用以监听
watch(propsCount, (newVal) => {
  if (newVal > 0){
    isShow.value = true
  }else {
    isShow.value = false
  }
})

</script>

<style lang="scss">
*{
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.counter-card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;

  .center-input {

    .el-input__inner {
      text-align: center;
    }
  }
}


</style>
