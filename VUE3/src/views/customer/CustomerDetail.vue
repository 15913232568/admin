<template>
  <div class="customer-detail">
    <!-- 操作按钮 -->
    <div class="action-bar">
      <div class="left-buttons">
        <el-button type="primary" @click="handleEdit">编辑</el-button>
        <el-button type="primary" plain @click="handleAssign">转派</el-button>
        <el-button type="primary" plain @click="handleRelease">释放</el-button>
      </div>
      <ActionButtons
        :user-role="userRole"
        :is-submitted="isSubmitted"
        @save="handleSave"
        @submit="handleSubmit"
        @reject="handleReject"
        @return="handleReturn"
        @approve="handleApprove"
      />
    </div>

    <!-- 基础信息 -->
    <el-card class="info-card" header="基础信息">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="客户ID">{{ customer?.customerId }}</el-descriptions-item>
        <el-descriptions-item label="CID">{{ customer?.cid }}</el-descriptions-item>
        <el-descriptions-item label="客户名称">{{ customer?.name }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ customer?.contact }}</el-descriptions-item>
        <el-descriptions-item label="渠道来源">{{ customer?.source }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ customer?.createdAt ? formatDate(customer?.createdAt) : '' }}</el-descriptions-item>
        <el-descriptions-item label="归属人">{{ customer?.owner }}</el-descriptions-item>
        <el-descriptions-item label="提交人">{{ customer?.submitter }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ customer?.status }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 客户意向 -->
    <el-card class="info-card" header="客户意向" style="margin-top: 20px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="意向产品">{{ customer?.intentProduct }}</el-descriptions-item>
        <el-descriptions-item label="意向度">
          <el-tag :type="getIntentLevelType(customer?.intentLevel)">{{ customer?.intentLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预算">{{ customer?.budget }}</el-descriptions-item>
        <el-descriptions-item label="预计成交时间">{{ customer?.expectedTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 需求信息 -->
    <el-card class="info-card" header="需求信息" style="margin-top: 20px">
      <el-table :data="formatRequirements(customer?.requirements || [])" style="width: 100%">
        <el-table-column prop="content" label="需求内容" />
      </el-table>
    </el-card>

    <!-- 跟进日志 -->
    <el-card class="info-card" header="跟进日志" style="margin-top: 20px">
      <div class="log-header">
        <el-button type="primary" size="small" @click="showLogDialog = true">添加日志</el-button>
      </div>
      <el-table :data="formatFollowLogs(customer?.followLogs || [])" style="width: 100%">
        <el-table-column prop="content" label="日志内容" />
      </el-table>
    </el-card>

    <!-- 添加日志弹窗 -->
    <el-dialog title="添加跟进日志" v-model="showLogDialog" width="500px">
      <el-form :model="logForm" label-width="80px">
        <el-form-item label="日志内容">
          <el-input type="textarea" v-model="logForm.content" rows="4" placeholder="请输入跟进内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showLogDialog = false">取消</el-button>
          <el-button type="primary" @click="handleAddLog">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import ActionButtons from '../../components/ActionButtons.vue'

// 路由
const route = useRoute()
const id = route.params.id as string

// 客户信息
const customer = ref<any>(null)

// 加载状态
const loading = ref(false)

// 添加日志弹窗
const showLogDialog = ref(false)

// 日志表单
const logForm = ref({
  content: ''
})

// 用户角色
const userRole = ref('maker') // 可以根据实际情况从登录状态或API获取

// 是否已提交
const isSubmitted = ref(false) // 可以根据实际情况从API获取

// 获取客户详情 - 调用后端接口 /leads/{id}
const fetchCustomerDetail = async () => {
  loading.value = true
  try {
    console.log('开始调用后端接口 /leads/', id)
    
    // 调用后端接口 /leads/{id}
    const response = await request.get(`/leads/${id}`)
    
    console.log('后端接口响应:', response)
    
    // 检查响应格式，兼容不同的后端响应结构
    if (response.code === 200) {
      customer.value = response.data
      console.log('客户详情数据:', customer.value)
      ElMessage.success('客户详情加载成功')
    } else {
      ElMessage.error(response.message || '获取客户详情失败')
    }
  } catch (error: any) {
    console.error('获取客户详情失败:', error)
    
    // 处理认证失败的情况
    if (error.code === 401) {
      ElMessage.warning('认证已过期，请重新登录')
    } else {
      ElMessage.error('获取客户详情失败，请检查网络连接')
      
      // 如果后端接口不可用，将客户详情设为null，不显示任何数据
      customer.value = null
    }
  } finally {
    loading.value = false
  }
}

// 处理编辑
const handleEdit = () => {
  // 编辑逻辑
  console.log('编辑客户:', id)
}

// 处理转派
const handleAssign = () => {
  // 转派逻辑
  console.log('转派客户:', id)
}

// 处理释放
const handleRelease = () => {
  // 释放逻辑
  console.log('释放客户:', id)
}

// 处理添加日志
const handleAddLog = () => {
  // 添加日志逻辑
  console.log('添加日志:', logForm.value)
  showLogDialog.value = false
  logForm.value = { content: '' }
}

// 处理保存
const handleSave = () => {
  // 保存逻辑
  console.log('保存客户信息:', id)
}

// 处理提交
const handleSubmit = () => {
  // 提交逻辑
  console.log('提交客户信息:', id)
  isSubmitted.value = true
}

// 处理拒绝
const handleReject = () => {
  // 拒绝逻辑
  console.log('拒绝客户信息:', id)
}

// 处理返回
const handleReturn = () => {
  // 返回逻辑
  console.log('返回客户信息:', id)
}

// 处理批准
const handleApprove = () => {
  // 批准逻辑
  console.log('批准客户信息:', id)
}

// 获取意向度标签类型
const getIntentLevelType = (level: string) => {
  switch (level) {
    case '高':
      return 'success'
    case '中':
      return 'warning'
    case '低':
      return 'info'
    default:
      return 'info'
  }
}

// 格式化需求信息为表格数据
const formatRequirements = (requirements: string[]) => {
  return requirements.map((req, index) => ({
    content: req
  }))
}

// 格式化跟进日志为表格数据
const formatFollowLogs = (logs: string[]) => {
  return logs.map((log, index) => ({
    content: log
  }))
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

// 初始化
onMounted(() => {
  fetchCustomerDetail()
})
</script>

<style scoped>
.customer-detail {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-buttons {
  display: flex;
  gap: 10px;
}

.info-card {
  margin-bottom: 20px;
}

.log-header {
  margin-bottom: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>