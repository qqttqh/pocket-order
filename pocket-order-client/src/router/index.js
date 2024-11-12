import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'menu',
      component: () => import('@/views/MenuView.vue')
    },
    {
      path: '/order',
      name: 'order',
      component: () => import('@/views/OrdersView.vue')
    },
    {
      path: '/setting',
      name: 'setting',
      component: () => import('@/views/SettingView.vue')
    }
  ]
})

export default router
