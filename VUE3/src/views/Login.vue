<template>
  <div class="login-container">
    <div class="login-form">
      <h2>客户管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../apis/auth'
import { STORAGE_KEYS } from '../config/env'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    try {
      // 调用真实的后端登录接口
      const result = await login({
        username: loginForm.username,
        password: loginForm.password
      })
      
      console.log('登录成功，token已保存:', result.token)
      
      // 确保token已保存到localStorage
      const savedToken = localStorage.getItem(STORAGE_KEYS.TOKEN)
      console.log('验证token保存状态:', savedToken)
      
      // 登录成功，获取跳转路径
      const redirect = router.currentRoute.value.query.redirect as string
      const targetPath = redirect || '/customer/list'
      
      // 添加短暂延迟，确保token保存完成
      setTimeout(() => {
        // 跳转到目标页面
        router.push(targetPath)
        ElMessage.success('登录成功')
      }, 100)
      
    } catch (error: any) {
      // 登录失败，显示错误信息
      console.error('登录请求失败:', error)
      ElMessage.error(error.message || '登录失败，请检查网络连接')
      loading.value = false
    }
    
  } catch (error) {
    console.error('表单验证失败:', error)
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: var(--bg-color);
}

.login-form {
  width: 400px;
  padding: 40px;
  background-color: var(--white);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--primary-color);
  font-size: 24px;
  font-weight: 600;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button--primary {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
}

.el-button--primary:hover {
  background-color: #0052A3 !important;
  border-color: #0052A3 !important;
}
</style>