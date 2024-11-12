<template>
<div class="dishes-status-user-root">
  <span class="dish-name">{{ orderItem.name }}</span>
  <div class="count-container">
    <span class="dish-count">x{{ orderItem.count }}</span>
    <span v-if="userStore.data.role !== 'ADMIN'" v-show="orderItem.count !== orderItem.success" class="dish-success">{{ orderItem.success }}</span>
    <el-icon v-show="orderItem.count === orderItem.success"><IconSuccess/></el-icon>
    <el-input-number v-if="userStore.data.role === 'ADMIN' && order.status === 0" class="custom-el-input-number" :min="0" :max="orderItem.count" v-model="successCount" size="small" />
  </div>
</div>
</template>

<script setup>
import {defineProps, ref, watch} from 'vue'
import {useUserStore} from "@/stores/user.js";
import IconSuccess from "@/components/icons/IconSuccess.vue";

const userStore = useUserStore()

const props = defineProps({
  orderItem: Object,
  order: Object
})

const emits =  defineEmits(['updateProgress'])

const successCount = ref(0) // 完成数量
successCount.value = props.orderItem.success

watch(successCount, (newValue) => {
  emits('updateProgress', props.orderItem.id, newValue)
})

</script>

<style  lang="scss">
.dishes-status-user-root{
  display: flex;
  justify-content: space-between;
  color: #6C6C6C;
  padding: 2px 0;

  .count-container{
    display: flex;
    align-items: center;

    .dish-count{
      margin-right: 40px;
    }

    .custom-el-input-number{
      width: auto !important;
      margin-left: 20px;
    }
  }

  .dish-success{
    color: #0022AB;
    margin-right: 3px !important;
  }
}
</style>
