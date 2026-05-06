<template>
  <div class="public-customer">
    <h2>公海客户</h2>
    
    <!-- 操作栏 -->
    <div class="action-bar" v-if="!showEditForm">
      <el-button type="primary" @click="handleAdd">新增客户</el-button>
    </div>
    
    <!-- 客户列表 -->
    <div v-if="!showEditForm">
      <el-table :data="customerList" style="width: 100%">
        <el-table-column prop="customerId" label="客户ID" width="120" />
        <el-table-column prop="name" label="客户名称" width="150" />
        <el-table-column prop="contact" label="联系方式" width="150" />
        <el-table-column prop="source" label="客户来源" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleDetail(scope.row.id)">详情</el-button>
            <el-button size="small" type="primary" @click="handleClaim(scope.row.id)">认领</el-button>
            <el-button size="small" type="success" @click="handleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 客户编辑表单 -->
    <div v-if="showEditForm">
      <CustomerEditForm
        ref="editFormRef"
        :user-role="userRole"
        :is-submitted="isSubmitted"
        :customer-data="currentCustomer"
        @save="handleSave"
        @submit="handleSubmit"
        @cancel="handleCancelEdit"
        @reject="handleReject"
        @return="handleReturn"
        @approve="handleApprove"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import CustomerEditForm from '../../components/CustomerEditForm.vue'
import request from '../../utils/request'

const router = useRouter()

// 客户列表
const customerList = ref<any[]>([])

// 加载客户列表
const loadCustomerList = async () => {
  loading.value = true
  try {
    // 构建查询参数 - 用于获取公海客户（未分配的客户）
    const queryParams = {
      pageNum: 1,
      pageSize: 100, // 可根据需要调整
      sortField: 'createdAt',
      sortDir: 'desc',
      owner: 'NA'  // 查询未被认领的客户
    }
    
    // 调用后端API获取客户列表
    const response = await request.post('/leads/list', queryParams)
    
    if (response.code === 200) {
      const data = response.data
      customerList.value = data.records || data.list || [] // 根据实际响应结构调整
    } else {
      // 如果API返回错误码，则将列表设为空数组
      customerList.value = []
      ElMessage.error(response.message || '获取客户列表失败')
    }
  } catch (error: any) {
    // 如果发生异常（如网络错误），则将列表设为空数组
    customerList.value = []
    console.error('获取客户列表失败:', error)
    ElMessage.error('获取客户列表失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
onMounted(async () => {
  await loadCustomerList()
})

// 显示编辑表单
const showEditForm = ref(false)

// 当前编辑的客户
const currentCustomer = reactive({
  id: '',
  customerId: '',
  cid: '',
  name: '',
  contact: '',
  source: '',
  owner: '',
  submitter: '',
  status: '待跟进',
  intentProduct: '',
  intentLevel: '中',
  budget: '',
  expectedTime: '',
  requirements: [],
  followLogs: []
})

// 用户角色
const userRole = ref('maker')

// 是否已提交
const isSubmitted = ref(false)

// 处理详情
const handleDetail = (id: string) => {
  router.push(`/customer/detail/${id}`)
}

// 处理认领
const handleClaim = async (id: string) => {
  try {
    // 调用后端API认领客户
    const response = await request.put(`/leads/${id}/owner`)
    
    if (response.code === 200) {
      ElMessage.success('认领客户成功')
      // 重新加载客户列表以反映变化
      await loadCustomerList()
    } else {
      ElMessage.error(response.message || '认领客户失败')
    }
  } catch (error: any) {
    console.error('认领客户失败:', error)
    ElMessage.error('认领客户失败，请检查网络连接')
  }
}

// 处理新增
const handleAdd = () => {
  // 清空当前客户数据
  Object.assign(currentCustomer, {
    id: Date.now().toString(),
    customerId: '',
    cid: '',
    name: '',
    contact: '',
    source: '',
    owner: '',
    submitter: '',
    status: '待跟进',
    intentProduct: '',
    intentLevel: '中',
    budget: '',
    expectedTime: '',
    requirements: [],
    followLogs: []
  })
  
  showEditForm.value = true
  isSubmitted.value = false
}

// 处理编辑
const handleEdit = (row: any) => {
  // 复制客户数据到编辑表单
  Object.assign(currentCustomer, {
    ...row,
    requirements: Array.isArray(row.requirements) ? [...row.requirements] : [],
    followLogs: Array.isArray(row.followLogs) ? [...row.followLogs] : []
  })
  
  showEditForm.value = true
  isSubmitted.value = false
}

// 加载状态
const loading = ref(false)

// 处理保存
const handleSave = async (customerData: any) => {
  loading.value = true
  try {
    console.log('保存客户数据:', customerData)
    
    // 转换数据类型，确保与后端API匹配
    const formattedData = {
      ...customerData,
      // 设置owner默认值为NA
      owner: customerData.owner || 'NA',
      // 确保budget是数字类型
      budget: customerData.budget ? Number(customerData.budget) : null,
      // 确保expectedTime是正确的日期格式
      expectedTime: customerData.expectedTime ? customerData.expectedTime : null
    }
    
    console.log('格式化后的数据:', formattedData)
    
    let response;
    if (customerData.id) {
      // 如果存在ID，则执行更新操作
      response = await request.put(`/leads/${customerData.id}`, formattedData)
    } else {
      // 如果不存在ID，则执行创建操作
      response = await request.post('/leads', formattedData)
    }
    
    console.log('后端接口响应:', response)
    
    // 检查响应格式
    if (response.code === 200) {
      ElMessage.success('保存成功')
      showEditForm.value = false
      
      // 重新加载客户列表
      loadCustomerList()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error: any) {
    console.error('保存客户失败:', error)
    // 显示更详细的错误信息
    if (error.code && error.message) {
      ElMessage.error(`保存失败: ${error.message}`)
    } else {
      ElMessage.error('保存失败，请检查网络连接')
    }
  } finally {
    loading.value = false
  }
}

// 处理提交
const handleSubmit = async (customerData: any) => {
  loading.value = true
  try {
    console.log('提交客户数据:', customerData)
    
    // 转换数据类型，确保与后端API匹配
    const formattedData = {
      ...customerData,
      // 设置owner默认值为NA
      owner: customerData.owner || 'NA',
      // 确保budget是数字类型
      budget: customerData.budget ? Number(customerData.budget) : null,
      // 确保expectedTime是正确的日期格式
      expectedTime: customerData.expectedTime ? customerData.expectedTime : null
    }
    
    console.log('格式化后的数据:', formattedData)
    
    let response;
    if (customerData.id) {
      // 如果存在ID，则执行更新操作
      response = await request.put(`/leads/${customerData.id}`, formattedData)
    } else {
      // 如果不存在ID，则执行创建操作
      response = await request.post('/leads', formattedData)
    }
    
    console.log('后端接口响应:', response)
    
    // 检查响应格式
    if (response.code === 200) {
      isSubmitted.value = true
      ElMessage.success('提交成功')
      showEditForm.value = false
      
      // 重新加载客户列表
      loadCustomerList()
    } else {
      ElMessage.error(response.message || '提交失败')
    }
  } catch (error: any) {
    console.error('提交客户失败:', error)
    // 显示更详细的错误信息
    if (error.code && error.message) {
      ElMessage.error(`提交失败: ${error.message}`)
    } else {
      ElMessage.error('提交失败，请检查网络连接')
    }
  } finally {
    loading.value = false
  }
}

// 处理取消编辑
const handleCancelEdit = () => {
  showEditForm.value = false
  ElMessage.info('已取消编辑')
}

// 处理拒绝
const handleReject = () => {
  ElMessage.success('拒绝成功')
  showEditForm.value = false
}

// 处理返回
const handleReturn = () => {
  ElMessage.success('返回成功')
  showEditForm.value = false
}

// 处理批准
const handleApprove = () => {
  ElMessage.success('批准成功')
  showEditForm.value = false
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).replace(/\//g, '-')
}
</script>

<style scoped>
.public-customer {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.public-customer h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--text-color);
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
}
</style>