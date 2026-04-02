<template>
  <div class="public-customer">
    <h2>公海客户</h2>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">新增客户</el-button>
    </div>
    
    <!-- 客户列表 -->
    <div v-if="!showEditForm">
      <el-table :data="customerList" style="width: 100%">
        <el-table-column prop="customerId" label="客户ID" width="120" />
        <el-table-column prop="name" label="客户名称" width="150" />
        <el-table-column prop="contact" label="联系方式" width="150" />
        <el-table-column prop="source" label="客户来源" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
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

const router = useRouter()

// 客户列表
const customerList = ref<any[]>([])

// 加载客户列表 - 公海客户页面使用静态数据
const loadCustomerList = () => {
  // 使用静态数据，不调用API
  customerList.value = [
    {
      id: '3',
      customerId: 'C003',
      name: '王五',
      contact: '13800138003',
      source: '线上',
      createTime: '2024-01-03 10:00:00',
      owner: '张三',
      submitter: '李四',
      status: '待跟进',
      intentProduct: '产品A',
      intentLevel: '中',
      budget: '10000',
      expectedTime: '2026-02-01',
      requirements: [
        { id: '1', content: '需要定制化功能', createTime: '2024-01-03 11:00:00' }
      ],
      followLogs: [
        { id: '1', content: '初次接触，客户表示有兴趣', creator: '张三', createTime: '2024-01-03 10:30:00' }
      ]
    },
    {
      id: '4',
      customerId: 'C004',
      name: '赵六',
      contact: '13800138004',
      source: '线下',
      createTime: '2024-01-04 10:00:00',
      owner: '王五',
      submitter: '赵六',
      status: '跟进中',
      intentProduct: '产品B',
      intentLevel: '高',
      budget: '20000',
      expectedTime: '2026-02-15',
      requirements: [
        { id: '2', content: '需要技术支持', createTime: '2024-01-04 11:00:00' }
      ],
      followLogs: [
        { id: '2', content: '客户已参观公司，意向明确', creator: '王五', createTime: '2024-01-04 10:30:00' }
      ]
    }
  ]
}

// 组件挂载时加载数据
onMounted(() => {
  loadCustomerList()
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
const handleClaim = (id: string) => {
  ElMessage.success(`认领客户 ${id} 成功`)
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
    requirements: [...row.requirements],
    followLogs: [...row.followLogs]
  })
  
  showEditForm.value = true
  isSubmitted.value = false
}

// 处理保存
const handleSave = (customerData: any) => {
  // 使用静态数据，不调用API
  console.log('保存客户数据:', customerData)
  
  ElMessage.success('保存成功')
  showEditForm.value = false
  
  // 重新加载客户列表
  loadCustomerList()
}

// 处理提交
const handleSubmit = (customerData: any) => {
  // 使用静态数据，不调用API
  console.log('提交客户数据:', customerData)
  
  isSubmitted.value = true
  ElMessage.success('提交成功')
  showEditForm.value = false
  
  // 重新加载客户列表
  loadCustomerList()
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