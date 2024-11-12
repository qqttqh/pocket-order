<template>
  <div class="dishes-admin-card-root" v-if="thisGroupId === data.groupId">
    <aside>
      <el-avatar @click="triggerFileInput" shape="square" :size="80" :src="form.img" />
      <input type="file" ref="fileInput" @change="handleFileChange" style="display: none;" accept="image/*" />
    </aside>
    <main>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="菜名" prop="name">
          <el-input v-model="form.name" size="small" placeholder="菜名" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number class="custom-el-input-number" :controls="false" v-model="form.price" size="small" placeholder="价格" />
        </el-form-item>
        <el-form-item label="选择分组" prop="groupId">
          <el-select v-model="form.groupId" size="small" placeholder="选择分组">
            <el-option v-for="item in groupData" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.introduce" placeholder="介绍" type="textarea" :autosize="{ minRows: 3, maxRows: 4}" />
        </el-form-item>
        <el-form-item class="custom-el-form-item">
          <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              :icon="IconDelete"
              icon-color="#626AEF"
              title="确定删除菜品？"
              @confirm="menuStore.deleteDishes(id)"
          >
            <template #reference>
              <el-button color="#DC362E">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button :loading="saveBtnLoading" :disabled="saveBtnDisable" color="#0022AB" class="custom-el-button" type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </main>
  </div>
</template>

<script setup>
import {defineProps, ref, watch} from 'vue'
import {useMenuStore} from "@/stores/menu.js";
import IconDelete from "@/components/icons/IconDelete.vue";
import {uploadFileToOSS} from "@/utils/ossUtil.js";
import {uploadImage} from "@/utils/uploadUtil.js";

const saveBtnDisable = ref(true)
const saveBtnLoading = ref(false)
const formRef = ref(null)

const props = defineProps({
  thisGroupId: Number,
  data: Object
})
const menuStore = useMenuStore()

const groupData = menuStore.menuGroup
const id = props.data.id
const form = ref({
  name: props.data.name,
  img: props.data.img,
  price: Number(props.data.price),
  groupId: props.data.groupId,
  introduce: props.data.introduce,
})
watch(form.value, ()=>{
  saveBtnDisable.value = false
})

const rules = {
  name: [{ required: true, message: '菜名不能为空', trigger: 'blur' }],
  price: [
    { required: true, message: '价格不能为空', trigger: 'blur' },
    { type: 'number', message: '价格必须是数字', trigger: 'blur' }
  ],
  groupId: [{ required: true, message: '请选择分组', trigger: 'change' }],
};


// 引用隐藏的文件输入
const fileInput = ref(null);
const selectedFile = ref(null);

// 触发隐藏的文件输入
const triggerFileInput = () => {
  fileInput.value.click();
};

// 处理文件选择
const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    selectedFile.value = file;
    const reader = new FileReader();
    reader.onload = () => {
      form.value.img = reader.result;
    };
    reader.readAsDataURL(file);
  }
};

// 保存数据
const handleSave = async () => {
  saveBtnLoading.value = true;

  // 校验表单
  const valid = await formRef.value.validate();
  if (!valid) {
    saveBtnLoading.value = false;
    return; // 如果验证不通过，返回
  }

  if (selectedFile.value) {
    form.value.img = await uploadFileToOSS(selectedFile.value);
    selectedFile.value = null;
  }

  const data = {
    id: id,
    ...form.value, // 直接使用表单数据
  };

  await menuStore.updateDishesInfo(data);
  saveBtnLoading.value = false;
};


</script>

<style lang="scss">
  .dishes-admin-card-root {
    display: flex;
    padding: 20px;
    background-color: #fff;
    border-radius: 15px;
    //box-shadow: 0 0 10px rgba(0,0,0,.1);
    border: 1px solid #ddd;

    > aside {
      display: flex;
      flex-direction: column;
    }

    > main {
      padding-left: 20px;

      .el-form {

        .el-form-item{
          margin-bottom: 10px !important;
        }

        .custom-el-form-item {

          .el-form-item__content {
            display: flex;
            justify-content: end;
          }
        }
      }

      .custom-el-input-number{
        width: 100%;
        .el-input__wrapper{
          padding: 1px 10px !important;
          .el-input__inner{
            text-align: start !important;
          }
        }
      }
    }
  }
</style>
