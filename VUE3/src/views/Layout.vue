<template>
  <div class="layout">
    <Sidebar ref="sidebarRef" />
    <div class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
      <header class="header">
        <h1>{{ $route.meta.title }}</h1>
      </header>
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import Sidebar from '../components/Sidebar.vue'

const sidebarRef = ref<InstanceType<typeof Sidebar> | null>(null)
const isSidebarCollapsed = ref(false)

// 监听侧边栏折叠状态
watch(() => sidebarRef.value?.isCollapsed, (newValue) => {
  if (newValue !== undefined) {
    isSidebarCollapsed.value = newValue
  }
})
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transition: all 0.3s ease;
}

.header {
  height: var(--header-height);
  background-color: var(--white);
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.header h1 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-color);
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: var(--bg-color);
}
</style>