import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { isLoggedIn } from '../apis/auth'
import { STORAGE_KEYS } from '../config/env'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    redirect: '/customer/list'
  },
  {
    path: '/customer',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'list',
        component: () => import('../views/customer/CustomerList.vue'),
        meta: { title: '客户管理', requiresAuth: true }
      },
      {
        path: 'detail/:id',
        component: () => import('../views/customer/CustomerDetail.vue'),
        meta: { title: '客户详情', requiresAuth: true }
      },
      {
        path: 'public',
        component: () => import('../views/customer/PublicCustomer.vue'),
        meta: { title: '公海客户', requiresAuth: true }
      }
    ]
  },
  {
    path: '/system',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'settings',
        component: () => import('../views/system/Settings.vue'),
        meta: { title: '系统设置', requiresAuth: true }
      },
      {
        path: 'permission',
        component: () => import('../views/system/Permission.vue'),
        meta: { title: '权限管理', requiresAuth: true }
      },
      {
        path: 'user',
        component: () => import('../views/system/User.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/delivery',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'list',
        component: () => import('../views/delivery/DeliveryList.vue'),
        meta: { title: '销售交付', requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 定义不需要认证的白名单路由
  const publicRoutes = ['/login']
  
  // 检查是否需要认证
  if (to.meta.requiresAuth && !publicRoutes.includes(to.path)) {
    // 检查是否已登录
    if (isLoggedIn()) {
      next()
    } else {
      // 未登录，保存目标路由并跳转到登录页
      const redirectPath = to.fullPath === '/' ? '/customer/list' : to.fullPath
      next(`/login?redirect=${encodeURIComponent(redirectPath)}`)
    }
  } else {
    next()
  }
})

export default router