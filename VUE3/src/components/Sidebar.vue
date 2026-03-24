<template>
  <aside class="sidebar" :class="{ 'collapsed': isCollapsed }">
    <div class="logo">
      <h2 v-if="!isCollapsed">客户管理系统</h2>
    </div>
    <el-menu
      :default-active="activeMenu"
      class="sidebar-menu"
      router
      background-color="#0066CC"
      text-color="#FFFFFF"
      active-text-color="#FFFFFF"
      :collapse="isCollapsed"
    >
      <el-menu-item index="/customer/list">
        <el-icon><User /></el-icon>
        <span>客户管理</span>
      </el-menu-item>
      <el-menu-item index="/customer/public">
        <el-icon><Briefcase /></el-icon>
        <span>公海客户</span>
      </el-menu-item>
      <el-menu-item index="/system/settings">
        <el-icon><Setting /></el-icon>
        <span>系统设置</span>
      </el-menu-item>
      <el-menu-item index="/system/permission">
        <el-icon><Lock /></el-icon>
        <span>权限管理</span>
      </el-menu-item>
      <el-menu-item index="/system/user">
        <el-icon><UserFilled /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="/delivery/list">
        <el-icon><Van /></el-icon>
        <span>销售交付</span>
      </el-menu-item>
    </el-menu>
    <div class="collapse-btn" @click="toggleCollapse">
      <el-icon v-if="!isCollapsed"><ArrowLeft /></el-icon>
      <el-icon v-else><ArrowRight /></el-icon>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { User, Briefcase, Setting, Lock, Van, ArrowLeft, ArrowRight, UserFilled } from '@element-plus/icons-vue'

const route = useRoute()
const isCollapsed = ref(false)

const activeMenu = computed(() => {
  return route.path
})

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

// 暴露变量给父组件
defineExpose({
  isCollapsed
})
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  height: 100vh;
  background-color: var(--primary-color);
  overflow-y: auto;
  transition: width 0.3s ease;
  position: relative;
}

.sidebar.collapsed {
  width: 64px;
}

.logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.logo h2 {
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
  margin: 0;
}

.sidebar-menu {
  border-right: none;
  height: calc(100vh - var(--header-height) - 40px);
}

.sidebar-menu .el-menu-item {
  height: 50px;
  line-height: 50px;
  margin: 0;
}

.sidebar-menu .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.2) !important;
}

.collapse-btn {
  position: absolute;
  bottom: 20px;
  right: 10px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.collapse-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.collapse-btn .el-icon {
  color: #FFFFFF;
  font-size: 16px;
}
</style>