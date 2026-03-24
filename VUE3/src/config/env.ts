// 环境配置
const isDevelopment = import.meta.env.MODE === 'development'

// 开发环境下使用相对路径，让Vite代理处理跨域
export const API_BASE_URL = isDevelopment 
  ? '/api' 
  : 'http://localhost:8081/api'

export const STORAGE_KEYS = {
  TOKEN: 'token',
  USERNAME: 'username',
  ROLE: 'role'
}