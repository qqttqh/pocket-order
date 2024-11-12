<template>
  <el-anchor ref="anchor" :affix="false" class="custom-anchor" :container="container">
    <el-anchor-link v-for="item in menuGroup" :href="`#group${item.id}`" :title="item.name" class="custom-anchor-link"></el-anchor-link>
  </el-anchor>
</template>

<script setup>
import {computed, defineProps, ref, watch} from 'vue'

const props = defineProps({
  menuGroup: Array,
  container: Object,
  searchAnchor: String
})

const anchor = ref(null)
const searchAnchorProps = computed(()=>props.searchAnchor)

watch(searchAnchorProps, (newVal) => {
  anchor.value.scrollTo(newVal)
})

</script>

<style lang="scss">
.custom-anchor {
  background-color: transparent;

  .custom-anchor-link {
    display: block;
    margin-bottom: 10px;
    .el-anchor__link {
      font-size: 16px; /* 自定义字体大小 */
    }
  }
}
</style>
