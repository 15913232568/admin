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
        <el-descriptions-item label="创建时间">{{ customer?.createTime }}</el-descriptions-item>
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
      <el-table :data="customer?.requirements || []" style="width: 100%">
        <el-table-column prop="id" label="需求ID" width="100" />
        <el-table-column prop="content" label="需求内容" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-card>

    <!-- 跟进日志 -->
    <el-card class="info-card" header="跟进日志" style="margin-top: 20px">
      <div class="log-header">
        <el-button type="primary" size="small" @click="showLogDialog = true">添加日志</el-button>
      </div>
      <el-table :data="customer?.followLogs || []" style="width: 100%">
        <el-table-column prop="id" label="日志ID" width="100" />
        <el-table-column prop="content" label="日志内容" />
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
import { getCustomerDetail } from '../../apis/customer'
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

// 获取客户详情
const fetchCustomerDetail = async () => {
  loading.value = true
  try {
    const response = await getCustomerDetail(id)
    customer.value = response.data
  } catch (error) {
    console.error('获取客户详情失败:', error)
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