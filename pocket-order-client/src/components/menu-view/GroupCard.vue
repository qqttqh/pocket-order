<template>
  <div class="group-card-root" :id="`group${groupId}`">
    <DishesCard v-for="(item, index) in dishes" :data="item" :key="index"/>
  </div>
</template>

<script setup>
import DishesCard from "@/components/menu-view/DishesCard.vue";
import {useMenuStore} from "@/stores/menu.js";
import {computed, defineProps, ref, watch} from 'vue'

const menuStore = useMenuStore()
const dishes = ref([])

const props = defineProps({
  groupId: Number
})

dishes.value = menuStore.menuData.filter(item => item.groupId === props.groupId)

const searchKeyword = computed(()=>{
  return menuStore.searchKeyword
})
watch(searchKeyword,(newVal)=>{
  console.log(newVal)
  if (newVal){
    dishes.value = menuStore.searchResult.filter(item => item.groupId === props.groupId)
  }else{
    dishes.value = menuStore.menuData.filter(item => item.groupId === props.groupId)
  }

})
// dishes.value = props.data.filter(item => item.groupId === props.groupId)

</script>

<style scoped lang="scss">
.group-card-root {
  width: 100%;
}
</style>
