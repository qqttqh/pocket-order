import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端 API 地址
        changeOrigin: true,               // 跨域
        rewrite: (path) => path.replace(/^\/api/, ''), // 去掉 /api 前缀
        ws: true,                         // 启用 WebSocket 代理
      }
    }
  }
})
