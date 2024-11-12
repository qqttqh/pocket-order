<template>
  <div class="menu-button-container" :class="{'show-root': selfExpansion}">
    <!--菜单按钮-->
    <div class="menu-button" @click="switchSelfExpansion">
      <el-badge :value="menuStore.selectedTotal" :show-zero="false" :offset="[0,-6]" class="custom-el-badge" type="primary">
        <IconMenu/>
      </el-badge>
    </div>
    <!--总计价格-->
    <div class="total-container" :class="{'show': selfExpansion}" @click="switchExpansion">
      <span class="total-text">总计到手<span>¥ </span></span>
      <span class="total-number"> {{menuStore.selectedTotalPrice}}</span>
    </div>
    <!--下单按钮-->
    <el-popconfirm
        confirm-button-text="确认"
        cancel-button-text="取消"
        icon-color="#0022Ab"
        title="确认下单？"
        @confirm="odersStore.createOrder()"
    >
      <template #reference>
        <div class="confirm-button" :class="{'show-confirm-button': selfExpansion}">
          <span>下单</span>
        </div>
      </template>
    </el-popconfirm>
  </div>
  <MenuCardDeputy @close="expansion = false" :expansion="expansion"/>
</template>

<script setup>
import IconMenu from "@/components/icons/IconMenu.vue";
import MenuCardDeputy from "@/components/menu-view/MenuCardDeputy.vue";
import {ref} from "vue";
import {useMenuStore} from "@/stores/menu.js";
import {useOrdersStore} from "@/stores/orders.js";

const odersStore = useOrdersStore()
const expansion = ref(false)
const selfExpansion = ref(false)
const menuStore = useMenuStore()
// 收展已点菜单
function switchExpansion() {
  expansion.value = !expansion.value
}
// 收展菜单条
function switchSelfExpansion() {
  if(selfExpansion.value && expansion.value){ // 菜单栏和菜单条都展开
    selfExpansion.value = false
    expansion.value = false
  }else if(menuStore.selectedTotal && !expansion.value && !selfExpansion.value) { // 有选菜品，菜单栏和菜单条都收起
    selfExpansion.value = true
    expansion.value = true
  }else{
      selfExpansion.value = !selfExpansion.value
  }
}
</script>

<style scoped lang="scss">
.show{
  display: flex !important;
}
.show-root{
  width: 90% !important;
  //overflow: hidden !important;
}
.show-confirm-button{
  width: 90px !important;
}
.menu-button-container{
  //position: relative;
  position: absolute;
  z-index: 10;
  bottom: 10px;
  left: 5%;
  width: 55px;
  height: 55px;
  display: flex;
  background-color: #fff;
  border-radius: 55px;
  border: none;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow: visible;
  transition: width .5s ease;
  white-space: nowrap;

  /*菜单按钮*/
  .menu-button {
    position: absolute;
    left: 0;
    top: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 55px;
    height: 55px;
    border-radius: 50%;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    .custom-el-badge{
      transform: translate(0, 2px);
    }
  }
  /*合计容器*/
  .total-container{
    display: flex;
    flex-wrap: nowrap;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    width: calc(100% - 55px - 90px);
    margin-left: 55px;
    white-space: nowrap;

    .total-text{
      font-size: 16px;
      font-weight: 600;

      > span{
        color: #F95731;
        vertical-align: bottom;
      }
    }

    .total-number {
      font-size: 22px;
      color: #F95731;
    }
  }
  /*下单按钮*/
  .confirm-button{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 90px;
    border-radius: 0 55px 55px 0;
    background-color: #0022AB;
    overflow: hidden;
    //transition: width 1s ease;

    span {
      color: #fff;
      font-size: 16px;
      font-weight: 600;
    }
  }
}
</style>
