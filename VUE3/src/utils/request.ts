import axios from 'axios'
import { API_BASE_URL, STORAGE_KEYS } from '../config/env'

// 创建axios实例
const request = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 登录接口不需要token，其他接口需要
    if (config.url !== '/auth/login') {
      const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数
    // 直接返回response.data，让调用方处理具体的数据结构
    return response.data
  },
  (error) => {
    // 超出 2xx 范围的状态码都会触发该函数
    console.error('请求错误:', error)
    
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      
      if (status === 401) {
        // 未授权，清除token但不自动跳转，让调用方处理
        localStorage.removeItem(STORAGE_KEYS.TOKEN)
        localStorage.removeItem(STORAGE_KEYS.USERNAME)
        localStorage.removeItem(STORAGE_KEYS.ROLE)
        console.warn('认证失败，token已清除，请重新登录')
        // 不再自动跳转，让调用方处理认证失败
      }
      
      return Promise.reject({
        code: status,
        message: data?.message || '请求失败',
        data: data
      })
    } else if (error.request) {
      // 请求已发出但没有收到响应
      return Promise.reject({
        code: -1,
        message: '网络错误，请检查网络连接'
      })
    } else {
      // 发送请求时出现错误
      return Promise.reject({
        code: -2,
        message: error.message || '请求配置错误'
      })
    }
  }
)

export default request