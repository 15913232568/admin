<template>
  <div class="user">
    <h2>用户管理</h2>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">添加用户</el-button>
    </div>
    
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="用户名">
          <el-input v-model="filterForm.username" placeholder="请输入用户名" style="width: 150px" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="filterForm.role" placeholder="请选择角色" style="width: 150px">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 用户表格 -->
    <el-table
      v-loading="loading"
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="用户ID" width="100" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="name" label="姓名" width="150" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="scope">
          <el-tag :type="getRoleType(scope.row.role)">{{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="200" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="300" fixed="right">
        <template #default="scope">
          <div style="display: flex; gap: 8px; align-items: center;">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="primary" @click="handlePermission(scope.row)">权限配置</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 添加/编辑用户弹窗 -->
    <el-dialog title="用户信息" v-model="dialogVisible" width="500px">
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" :prop="userForm.id ? '' : 'password'">
          <el-input type="password" v-model="userForm.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 权限配置弹窗 -->
    <el-dialog title="用户权限配置" v-model="permissionDialogVisible" width="600px">
      <div class="permission-config">
        <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
        <el-divider />
        <el-tree
          :data="permissionTree"
          show-checkbox
          node-key="id"
          default-expand-all
          :checked-keys="userPermissions"
          @check="handlePermissionCheck"
        >
          <template #default="{ node }">
            <span>{{ node.label }}</span>
            <span v-if="node.code" class="permission-code">({{ node.code }})</span>
          </template>
        </el-tree>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUserPermissions">保存权限</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 模拟数据
const mockUsers = [
  {
    id: '1',
    username: 'admin',
    name: '管理员',
    role: 'admin',
    email: 'admin@example.com',
    phone: '13800138000',
    createTime: '2024-01-01 10:00:00',
    permissions: ['1', '2', '3', '4', '5']
  },
  {
    id: '2',
    username: 'user1',
    name: '用户1',
    role: 'user',
    email: 'user1@example.com',
    phone: '13800138001',
    createTime: '2024-01-02 10:00:00',
    permissions: ['1']
  },
  {
    id: '3',
    username: 'user2',
    name: '用户2',
    role: 'user',
    email: 'user2@example.com',
    phone: '13800138002',
    createTime: '2024-01-03 10:00:00',
    permissions: ['1', '5']
  }
]

// 模拟权限树数据
const permissionTree = ref([
  {
    id: '1',
    label: '客户管理',
    code: 'customer:manage',
    children: [
      {
        id: '1-1',
        label: '查看客户',
        code: 'customer:view'
      },
      {
        id: '1-2',
        label: '添加客户',
        code: 'customer:add'
      },
      {
        id: '1-3',
        label: '编辑客户',
        code: 'customer:edit'
      },
      {
        id: '1-4',
        label: '删除客户',
        code: 'customer:delete'
      }
    ]
  },
  {
    id: '2',
    label: '系统设置',
    code: 'system:settings',
    children: [
      {
        id: '2-1',
        label: '基本设置',
        code: 'system:settings:basic'
      },
      {
        id: '2-2',
        label: '高级设置',
        code: 'system:settings:advanced'
      }
    ]
  },
  {
    id: '3',
    label: '权限管理',
    code: 'system:permission',
    children: [
      {
        id: '3-1',
        label: '查看权限',
        code: 'permission:view'
      },
      {
        id: '3-2',
        label: '添加权限',
        code: 'permission:add'
      },
      {
        id: '3-3',
        label: '编辑权限',
        code: 'permission:edit'
      },
      {
        id: '3-4',
        label: '删除权限',
        code: 'permission:delete'
      }
    ]
  },
  {
    id: '4',
    label: '用户管理',
    code: 'system:user',
    children: [
      {
        id: '4-1',
        label: '查看用户',
        code: 'user:view'
      },
      {
        id: '4-2',
        label: '添加用户',
        code: 'user:add'
      },
      {
        id: '4-3',
        label: '编辑用户',
        code: 'user:edit'
      },
      {
        id: '4-4',
        label: '删除用户',
        code: 'user:delete'
      },
      {
        id: '4-5',
        label: '配置权限',
        code: 'user:permission'
      }
    ]
  },
  {
    id: '5',
    label: '销售交付',
    code: 'delivery:manage',
    children: [
      {
        id: '5-1',
        label: '查看交付',
        code: 'delivery:view'
      },
      {
        id: '5-2',
        label: '添加交付',
        code: 'delivery:add'
      },
      {
        id: '5-3',
        label: '编辑交付',
        code: 'delivery:edit'
      },
      {
        id: '5-4',
        label: '删除交付',
        code: 'delivery:delete'
      }
    ]
  }
])

// 加载状态
const loading = ref(false)

// 用户列表
const userList = ref<any[]>([])

// 分页参数
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 筛选参数
const filterForm = reactive({
  username: '',
  role: ''
})

// 选中的用户ID
const selectedIds = ref<string[]>([])

// 弹窗状态
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)

// 用户表单
const userForm = reactive({
  id: '',
  username: '',
  name: '',
  password: '',
  role: 'user',
  email: '',
  phone: ''
})

// 当前编辑的用户
const currentUser = ref<any>(null)

// 用户权限
const userPermissions = ref<string[]>([])

// 全选状态
const selectAll = ref(false)

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'blur' }
  ]
}

// 表单引用
const userFormRef = ref()

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    // 模拟API请求
    setTimeout(() => {
      let filteredUsers = [...mockUsers]
      
      // 应用筛选
      if (filterForm.username) {
        filteredUsers = filteredUsers.filter(user => user.username.includes(filterForm.username))
      }
      if (filterForm.role) {
        filteredUsers = filteredUsers.filter(user => user.role === filterForm.role)
      }
      
      userList.value = filteredUsers
      pagination.total = filteredUsers.length
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
    loading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = (val: any[]) => {
  selectedIds.value = val.map(item => item.id)
}

// 处理搜索
const handleSearch = () => {
  fetchUserList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.username = ''
  filterForm.role = ''
  fetchUserList()
}

// 处理添加
const handleAdd = () => {
  userForm.id = ''
  userForm.username = ''
  userForm.name = ''
  userForm.password = ''
  userForm.role = 'user'
  userForm.email = ''
  userForm.phone = ''
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: any) => {
  userForm.id = row.id
  userForm.username = row.username
  userForm.name = row.name
  userForm.password = ''
  userForm.role = row.role
  userForm.email = row.email
  userForm.phone = row.phone
  dialogVisible.value = true
}

// 处理权限配置
const handlePermission = (row: any) => {
  currentUser.value = row
  userPermissions.value = row.permissions || []
  // 计算全选状态
  selectAll.value = userPermissions.value.length === getAllPermissionIds().length
  permissionDialogVisible.value = true
}

// 处理全选
const handleSelectAll = (val: boolean) => {
  if (val) {
    userPermissions.value = getAllPermissionIds()
  } else {
    userPermissions.value = []
  }
}

// 处理权限选择
const handlePermissionCheck = () => {
  // 计算全选状态
  selectAll.value = userPermissions.value.length === getAllPermissionIds().length
}

// 保存用户权限
const saveUserPermissions = () => {
  if (!currentUser.value) return
  
  // 模拟API请求
  setTimeout(() => {
    // 更新用户权限
    const userIndex = mockUsers.findIndex(user => user.id === currentUser.value.id)
    if (userIndex !== -1) {
      mockUsers[userIndex].permissions = [...userPermissions.value]
    }
    
    ElMessage.success('权限配置成功')
    permissionDialogVisible.value = false
    fetchUserList()
  }, 500)
}

// 处理删除
const handleDelete = async (_id: string) => {
  try {
    // 模拟API请求
    setTimeout(() => {
      ElMessage.success('删除成功')
      fetchUserList()
    }, 500)
  } catch (error) {
    console.error('删除用户失败:', error)
    ElMessage.error('删除用户失败')
  }
}

// 处理保存
const handleSave = async () => {
  if (!userFormRef.value) return
  
  try {
    // 模拟表单验证
    if (!userForm.username || !userForm.name || (!userForm.id && !userForm.password) || !userForm.role) {
      ElMessage.error('请填写必填字段')
      return
    }
    
    // 模拟API请求
    setTimeout(() => {
      if (userForm.id) {
        ElMessage.success('更新成功')
      } else {
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      fetchUserList()
    }, 500)
  } catch (error) {
    console.error('保存用户失败:', error)
    ElMessage.error('保存用户失败')
  }
}

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchUserList()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchUserList()
}

// 获取角色标签类型
const getRoleType = (role: string) => {
  return role === 'admin' ? 'success' : 'info'
}

// 获取所有权限ID
const getAllPermissionIds = () => {
  const ids: string[] = []
  
  const collectIds = (nodes: any[]) => {
    nodes.forEach(node => {
      ids.push(node.id)
      if (node.children && node.children.length > 0) {
        collectIds(node.children)
      }
    })
  }
  
  collectIds(permissionTree.value)
  return ids
}

// 初始化
onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.user {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.user h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--text-color);
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.filter-bar {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.permission-config {
  max-height: 400px;
  overflow-y: auto;
}

.permission-code {
  font-size: 12px;
  color: #909399;
  margin-left: 8px;
}
</style>