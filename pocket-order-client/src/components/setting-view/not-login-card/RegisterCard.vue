<template>
  <div @click="emits('close')" class="register-card-mask">
    <div @click.stop class="register-card-root">
      <div class="text-container">
        <h1>注册</h1>
        <h5>欢迎使用口袋点餐</h5>
      </div>
      <el-form :model="form" :rules="rules" ref="registerForm" label-width="0">
        <div class="input-container">
          <el-form-item prop="username">
            <el-input v-model="form.username" class="custom-el-input" :prefix-icon="IconUser" clearable placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" class="custom-el-input" :prefix-icon="IconPassword" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" class="custom-el-input" :prefix-icon="IconPassword" type="password" show-password placeholder="请确认密码" />
          </el-form-item>
          <el-button  @click="submitForm" :loading="isLoading" type="primary" size="large" color="#0022AB">注册</el-button>
        </div>
      </el-form>
      <el-divider border-style="dashed" />
      <div class="switch-container">
        <el-button @click="emits('switch')" round>去登录</el-button>
        <span>已经有账号了</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import IconUser from "@/components/icons/IconUser.vue";
import IconPassword from "@/components/icons/IconPassword.vue";
import { ElMessage } from 'element-plus';
import {useUserStore} from "@/stores/user.js";
import {useOrdersStore} from "@/stores/orders.js";

const emits = defineEmits(["close", "switch"]); // 关闭和登录注册切换事件
const userStore = useUserStore()
const ordersStore = useOrdersStore()
const isLoading = ref(false)

// 表单数据
const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
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
  ],
  confirmPassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    { validator: (rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const registerForm = ref(null);

// 提交表单
const submitForm = () => {
  registerForm.value.validate((valid) => {
    if (valid) {
      // 校验通过，进行注册操作
      const username = form.value.username;
      const password = form.value.password;
      isLoading.value = true;
      userStore.register(username, password)
          .then(() => {
            isLoading.value = false
            ordersStore.clearOrder()
            emits('close') // 关闭注册卡
          })
          .catch(() => {
            isLoading.value = false
          })
    }
  });
};
</script>


<style lang="scss">
.register-card-mask{
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0,0,0,0.1);

  .register-card-root {
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
