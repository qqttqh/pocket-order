<template>
<div class="online-count-root">
  <span class="title">🖊:</span>
  <span class="online-count">2人</span>
</div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.online-count-root {
  display: flex;
  align-items: end;
  border: 1px solid #E3E3E3;
  padding: 2px 10px;
  border-radius: 20px;

  .title{
    color: #183DF2;
    font-weight: 600;
  }

  .online-count {
    font-size: 16px;
  }

}
</style>
