import request from '../utils/request'
import { STORAGE_KEYS } from '../config/env'

// 登录请求参数
export interface LoginParams {
  username: string
  password: string
}

// 登录成功响应数据
export interface LoginSuccessResponse {
  token: string
  type: string
}

// 登录失败响应数据
export interface LoginErrorResponse {
  code: number
  message: string
}

// 登录接口
export const login = async (params: LoginParams): Promise<LoginSuccessResponse> => {
  try {
    // 发送登录请求
    const response: any = await request.post('/auth/login', params)
    
    // 登录成功，存储token到本地
    if (response.token) {
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.token)
      localStorage.setItem(STORAGE_KEYS.USERNAME, params.username)
      // 可以根据实际需求存储用户角色等信息
    }
    
    return {
      token: response.token,
      type: response.type || 'Bearer'
    }
  } catch (error: any) {
    console.error('登录请求失败:', error)
    
    // 处理登录失败的错误信息
    if (error.code === 401) {
      throw new Error(error.message || '用户名或密码错误')
    } else {
      throw new Error(error.message || '登录失败，请检查网络连接')
    }
  }
}

// 退出登录
export const logout = () => {
  localStorage.removeItem(STORAGE_KEYS.TOKEN)
  localStorage.removeItem(STORAGE_KEYS.USERNAME)
  localStorage.removeItem(STORAGE_KEYS.ROLE)
}

// 检查是否已登录
export const isLoggedIn = (): boolean => {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
  console.log('认证检查 - Token:', token)
  console.log('认证检查 - Token存在:', !!token)
  console.log('认证检查 - Token有效:', token && token !== 'null' && token !== 'undefined')
  return !!token && token !== 'null' && token !== 'undefined'
}

// 获取当前用户token
export const getToken = (): string | null => {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
  return token && token !== 'null' && token !== 'undefined' ? token : null
}

// 验证token有效性（简单版本，实际项目中可能需要调用后端验证接口）
export const validateToken = async (): Promise<boolean> => {
  const token = getToken()
  if (!token) return false
  
  // 这里可以添加token过期时间检查等逻辑
  // 实际项目中可能需要调用后端验证接口
  return true
}

// 清除所有认证信息
export const clearAuth = (): void => {
  localStorage.removeItem(STORAGE_KEYS.TOKEN)
  localStorage.removeItem(STORAGE_KEYS.USERNAME)
  localStorage.removeItem(STORAGE_KEYS.ROLE)
}