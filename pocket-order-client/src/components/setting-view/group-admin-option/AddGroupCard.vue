<template>
  <el-button @click="cardShow = true" :icon="IconAdd1" size="large" color="#0022AB" circle />
  <div @click.stop="cardShow = false" v-show="cardShow" class="add-group-card-root">
    <div @click.stop class="group-admin-card-root">
      <div class="input-container">
        <el-input class="custom-el-input" v-model="name" style="width: 90%" placeholder="分组名称"></el-input>
      </div>
      <el-button @click="addGroup" :disabled="saveBtnDisable" type="primary" color="#0022AB" :icon="IconSave" circle />
    </div>
  </div>
</template>

<script setup>

import {ref, watch} from "vue";
import IconAdd1 from "@/components/icons/IconAdd1.vue";
import IconSave from "@/components/icons/IconSave.vue";
import {useMenuStore} from "@/stores/menu.js";

const cardShow = ref(false)
const saveBtnDisable = ref(true)
const name = ref('')

const menuStore = useMenuStore()

watch(name,()=>{
  saveBtnDisable.value = false
})

function addGroup() {
  menuStore.addGroup(name.value)
  cardShow.value = false
}
</script>

<style scoped lang="scss">
*{
  box-sizing: border-box;
}
.add-group-card-root {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 3;
  width: 100vw;
  height: 100vh;
  max-width: 430px;
  min-width: 375px;
  padding: 0 10px;
  background-color: rgba(0, 0, 0, .1);

  .group-admin-card-root {
    display: flex;
    background-color: #fff;
    padding: 10px 20px;
    border-radius: 15px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 95%;

    .input-container {
      flex-grow: 1;

      .custom-el-input {

        > .el-input__wrapper {
          border-radius: 10px;
        }
      }
    }
  }
}
</style>
