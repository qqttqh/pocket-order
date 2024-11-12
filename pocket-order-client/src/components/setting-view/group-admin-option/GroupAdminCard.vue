<template>
<div class="group-admin-card-root">
  <div class="input-container">
    <el-input class="custom-el-input" v-model="name" style="width: 90%"></el-input>
  </div>
  <el-popconfirm
      width="220"
      :icon="IconDelete"
      icon-color="#626AEF"
      title="确认删除?"
      cancel-button-text="取消"
      confirm-button-text="确认"
      @confirm="menuStore.deleteGroup(id)"
  >
    <template #reference>
      <el-button type="primary" color="#E60023" :icon="IconDelete" circle />
    </template>
  </el-popconfirm>
  <el-button @click="menuStore.updateGroupInfo({id,name})" type="primary" color="#0022AB" :icon="IconSave" :disabled="saveDisable" circle />
</div>
</template>

<script setup>
import IconDelete from "@/components/icons/IconDelete.vue";
import IconSave from "@/components/icons/IconSave.vue";
import {ref, watch} from "vue";
import {useMenuStore} from "@/stores/menu.js";

const props = defineProps({
  data: Object
})
const menuStore = useMenuStore()
const saveDisable = ref(true)

const id = ref(props.data.id)
const name = ref(props.data.name);

watch(name, () => {
  saveDisable.value = false;
})


</script>

<style  lang="scss">
.group-admin-card-root {
  display: flex;
  background-color: #fff;
  padding: 10px 20px;
  border-radius: 15px;
  border: 1px solid #ddd;

  .input-container {
    flex-grow: 1;

    .custom-el-input {

      > .el-input__wrapper {
        border-radius: 10px;
      }
    }
  }
}
</style>
