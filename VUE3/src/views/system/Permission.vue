<template>
  <div class="permission">
    <h2>权限管理</h2>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">添加权限</el-button>
    </div>
    
    <!-- 权限表格 -->
    <el-table
      v-loading="loading"
      :data="permissionList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="权限ID" width="100" />
      <el-table-column prop="name" label="权限名称" width="150" />
      <el-table-column prop="code" label="权限编码" width="150" />
      <el-table-column prop="description" label="权限描述" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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
    
    <!-- 添加/编辑权限弹窗 -->
    <el-dialog title="权限信息" v-model="dialogVisible" width="500px">
      <el-form :model="permissionForm" :rules="rules" ref="permissionFormRef" label-width="80px">
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="permissionForm.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="permissionForm.code" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="权限描述" prop="description">
          <el-input type="textarea" v-model="permissionForm.description" rows="4" placeholder="请输入权限描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 模拟数据
const mockPermissions = [
  {
    id: '1',
    name: '客户管理',
    code: 'customer:manage',
    description: '客户管理权限',
    createTime: '2024-01-01 10:00:00'
  },
  {
    id: '2',
    name: '系统设置',
    code: 'system:settings',
    description: '系统设置权限',
    createTime: '2024-01-01 10:00:00'
  },
  {
    id: '3',
    name: '权限管理',
    code: 'system:permission',
    description: '权限管理权限',
    createTime: '2024-01-01 10:00:00'
  },
  {
    id: '4',
    name: '用户管理',
    code: 'system:user',
    description: '用户管理权限',
    createTime: '2024-01-01 10:00:00'
  },
  {
    id: '5',
    name: '销售交付',
    code: 'delivery:manage',
    description: '销售交付权限',
    createTime: '2024-01-01 10:00:00'
  }
]

// 加载状态
const loading = ref(false)

// 权限列表
const permissionList = ref<any[]>([])

// 分页参数
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 选中的权限ID
const selectedIds = ref<string[]>([])

// 弹窗状态
const dialogVisible = ref(false)

// 权限表单
const permissionForm = reactive({
  id: '',
  name: '',
  code: '',
  description: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' }
  ]
}

// 表单引用
const permissionFormRef = ref()

// 获取权限列表
const fetchPermissionList = async () => {
  loading.value = true
  try {
    // 模拟API请求
    setTimeout(() => {
      permissionList.value = mockPermissions
      pagination.total = mockPermissions.length
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('获取权限列表失败:', error)
    ElMessage.error('获取权限列表失败')
    loading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = (val: any[]) => {
  selectedIds.value = val.map(item => item.id)
}

// 处理添加
const handleAdd = () => {
  permissionForm.id = ''
  permissionForm.name = ''
  permissionForm.code = ''
  permissionForm.description = ''
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: any) => {
  permissionForm.id = row.id
  permissionForm.name = row.name
  permissionForm.code = row.code
  permissionForm.description = row.description
  dialogVisible.value = true
}

// 处理删除
const handleDelete = async (_id: string) => {
  try {
    // 模拟API请求
    setTimeout(() => {
      ElMessage.success('删除成功')
      fetchPermissionList()
    }, 500)
  } catch (error) {
    console.error('删除权限失败:', error)
    ElMessage.error('删除权限失败')
  }
}

// 处理保存
const handleSave = async () => {
  if (!permissionFormRef.value) return
  
  try {
    // 模拟表单验证
    if (!permissionForm.name || !permissionForm.code) {
      ElMessage.error('请填写必填字段')
      return
    }
    
    // 模拟API请求
    setTimeout(() => {
      if (permissionForm.id) {
        ElMessage.success('更新成功')
      } else {
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      fetchPermissionList()
    }, 500)
  } catch (error) {
    console.error('保存权限失败:', error)
    ElMessage.error('保存权限失败')
  }
}

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchPermissionList()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchPermissionList()
}

// 初始化
onMounted(() => {
  fetchPermissionList()
})
</script>

<style scoped>
.permission {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.permission h2 {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>