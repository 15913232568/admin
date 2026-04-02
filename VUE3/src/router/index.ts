import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { isLoggedIn } from '../apis/auth'

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
router.beforeEach((to, _from, next) => {
  console.log('路由守卫检查:', to.path, 'requiresAuth:', to.meta.requiresAuth)
  
  // 定义不需要认证的白名单路由
  // const publicRoutes = ['/login']
  
  // 如果访问的是登录页，直接放行
  if (to.path === '/login') {
    // 如果已登录，重定向到首页
    if (isLoggedIn()) {
      next('/')
    } else {
      next()
    }
    return
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    // 检查是否已登录
    const loggedIn = isLoggedIn()
    console.log('认证检查结果:', loggedIn)
    
    if (loggedIn) {
      next()
    } else {
      console.log('未登录，跳转到登录页')
      // 未登录，跳转到登录页
      next('/login')
    }
  } else {
    next()
  }
})

export default router