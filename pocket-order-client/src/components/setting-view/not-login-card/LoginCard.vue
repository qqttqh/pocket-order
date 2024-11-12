<template>
  <div @click="emits('close')" class="login-card-mask">
    <div @click.stop class="login-card-root">
      <div class="text-container">
        <h1>登录</h1>
        <h5>欢迎使用口袋点餐</h5>
      </div>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="0">
        <div class="input-container">
          <el-form-item prop="username">
            <el-input v-model="form.username" class="custom-el-input" :prefix-icon="IconUser" clearable placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" class="custom-el-input" :prefix-icon="IconPassword" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-button @click="submitForm" :loading="isLoading" color="#0022AB"  type="primary" size="large">登录</el-button>
        </div>
      </el-form>
      <el-divider border-style="dashed" />
      <div class="switch-container">
        <el-button @click="emits('switch')" round>去注册</el-button>
        <span>还没有账号</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import IconUser from "@/components/icons/IconUser.vue";
import IconPassword from "@/components/icons/IconPassword.vue";
import {useUserStore} from "@/stores/user.js";
import {useOrdersStore} from "@/stores/orders.js";

const emits = defineEmits(["close", "switch"]); // 关闭和登录注册切换事件
const userStore = useUserStore()
const orderStore = useOrdersStore()
const isLoading = ref(false)

// 表单数据
const form = ref({
  username: '',
  password: ''
});

// 表单校验规则
const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 15, message: "用户名长度应在 3 到 15 之间", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能小于 6 个字符", trigger: "blur" }
  ]
};

const loginForm = ref(null);

// 提交表单
const submitForm = () => {
  loginForm.value.validate((valid) => {
    if (valid) {
      // 校验通过，进行登录操作
      const username = form.value.username;
      const password = form.value.password;
      isLoading.value = true;
      userStore.login(username, password)
          .then(() => {
            isLoading.value = false
            orderStore.clearOrder()
            emits('close') // 关闭登录卡
          })
          .catch(() => {
            isLoading.value = false
          })
    }
  });
};
</script>


<style lang="scss">
.login-card-mask{
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0,0,0,0.1);
  
  .login-card-root {
    width: 80%;
    max-width: 375px;
    padding: 20px;
    border-radius: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: #fff;

    .text-container{

      h5{
        padding: 10px 0 20px;
        color: #6C6C6C;
      }
    }

    .input-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 3px;

      .custom-el-input {
        width: 250px;

        > .el-input__wrapper {
          border-radius: 20px !important;

        }
      }
    }

    .switch-container {
      display: flex;
      flex-direction: column;
      align-items: center;

      > span {
        font-size: 12px;
        color: #6C6C6C;
        margin-top: 10px;
      }
    }
  }
}

</style>
