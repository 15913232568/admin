<template>
  <div class="settings">
    <h2>系统设置</h2>
    <el-form label-width="120px">
      <el-form-item label="系统名称">
        <el-input v-model="settings.systemName" placeholder="请输入系统名称" />
      </el-form-item>
      <el-form-item label="系统版本">
        <el-input v-model="settings.systemVersion" placeholder="请输入系统版本" />
      </el-form-item>
      <el-form-item label="系统描述">
        <el-input type="textarea" v-model="settings.systemDescription" rows="4" placeholder="请输入系统描述" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存设置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 设置表单
const settings = ref({
  systemName: '客户管理系统',
  systemVersion: '1.0.0',
  systemDescription: '客户管理系统是一个用于管理客户信息的后台系统'
})

// 处理保存
const handleSave = () => {
  console.log('保存设置:', settings.value)
}
</script>

<style scoped>
.settings {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.settings h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--text-color);
}
</style>