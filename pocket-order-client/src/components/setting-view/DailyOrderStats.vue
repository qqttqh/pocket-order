<template>
  <div ref="info" class="charts-container"></div>
</template>

<script setup>
import { onMounted, ref, inject, onUnmounted, watch } from "vue";
import {useOrdersStore} from "@/stores/orders.js";

const orderStore = useOrdersStore()

//通过inject使用echarts
const echarts = inject("echarts");

//通过ref获取html元素
const info = ref(null);
let userEc = null;

const resizeChart = () => {
  if (userEc) {
    userEc.resize();
  }
};

onMounted(() => {
  // 渲染echarts的父元素
  var infoEl = info.value;

  //  light dark
  userEc = echarts.init(infoEl);

  // 指定图表的配置项和数据
  var option = {
    title: {
      text: '营业情况', // 主标题
      left: 'center', // 水平位置
      top: 'top', // 垂直位置
      textStyle: {
        color: '#333', // 主标题颜色
        fontSize: 18, // 主标题字体大小
        // fontWeight: 'bold' // 主标题字体粗细
      }
    },
    xAxis: {
      type: 'category',
      data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      axisLabel: {
        formatter: '{value} 日'
      },
      axisLine: {
        lineStyle: {
          color: '#AAAAAA' // 设置轴线颜色
        }
      },
      axisTick: {
        alignWithLabel: true // 刻度与标签对齐
      },
      boundaryGap: true // 去除边界空白
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} 元'
      },
      axisLine: {
        lineStyle: {
          color: '#AAAAAA'
        }
      },
      splitLine: {
        show: false // 去除网格线
      }
    },
    grid: {
      top: '13%', // 设置顶部内边距
      bottom: '0%', // 设置底部内边距
      left: '0%', // 设置左侧内边距
      right: '0%', // 设置右侧内边距
      containLabel: true,
      backgroundColor: '#fff', // 自定义背景颜色
      borderWidth: 1, // 设置边框宽度
      borderColor: '#ccc', // 设置边框颜色
      borderRadius: [100, 100, 100, 100] // 设置左上、右上、右下、左下的圆角半径
    },
    dataZoom: [{
      type: 'inside', // 滑动条型 dataZoom 组件
      show: true, // 显示滑动条
      start: 75, // 滑动条开始位置（0-100）
      end: 100, // 滑动条结束位置（0-100）
      bottom: '2%' // 位置调整到底部
    }],
    series: [{
      name: '单',
      data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      type: 'line',
      color: '#0022AB',
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      label: {
        show: true,
        position: 'top',
        color: 'black',
        formatter: '{c} 单',
      }
    }, {
      name: '元',
      data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      type: 'line',
      color: '#f8b500',
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      label: {
        show: true,
        position: 'top',
        color: 'black',
        formatter: '{c} 元',
      }
    }]
  };

  // 使用刚指定的配置项和数据显示图表。
  userEc.setOption(option);
  userEc.resize(); // 初始渲染时调整图表大小
  window.addEventListener('resize', resizeChart); // 监听窗口大小变化事件

  // 通过 watch 监听数据是否准备好，当数据准备好时执行回调
  watch(
      [
        () => orderStore.dateList,
        () => orderStore.totalOrdersList,
        () => orderStore.totalSalesList
      ],
      ([dateList, totalOrdersList, totalSalesList]) => {
        if (dateList.length > 0 && totalOrdersList.length > 0 && totalSalesList.length > 0) {
          userEc.setOption({
            xAxis: {
              data: dateList
            },
            series: [
              {
                data: totalOrdersList
              },
              {
                data: totalSalesList
              }
            ]
          });
        }
      },
      { immediate: true }
  );

});

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart); // 组件销毁时移除事件监听器
  if (userEc) {
    userEc.dispose(); // 销毁 ECharts 实例
  }
});
</script>


<style scoped lang="scss">
.charts-container {
  width: 100%;
  height: 200px;
  background-color: #fff;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
}
</style>
