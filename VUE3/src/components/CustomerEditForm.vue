<template>
  <div class="customer-edit-form">
    <!-- 操作按钮 -->
    <div class="action-bar">
      <div class="left-buttons">
        <el-button type="primary" plain @click="handleCancel">取消</el-button>
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
      <el-form :model="customerForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户ID">
              <el-input v-model="customerForm.customerId" placeholder="请输入客户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="CID">
              <el-input v-model="customerForm.cid" placeholder="请输入CID" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户名称">
              <el-input v-model="customerForm.name" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="联系方式">
              <el-input v-model="customerForm.contact" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="渠道来源">
              <el-select v-model="customerForm.source" placeholder="请选择渠道来源" style="width: 100%">
                <el-option label="线上" value="线上" />
                <el-option label="线下" value="线下" />
                <el-option label="推荐" value="推荐" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="归属人">
              <el-input v-model="customerForm.owner" placeholder="请输入归属人" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="提交人">
              <el-input v-model="customerForm.submitter" placeholder="请输入提交人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-select v-model="customerForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="待跟进" value="待跟进" />
                <el-option label="跟进中" value="跟进中" />
                <el-option label="已成交" value="已成交" />
                <el-option label="已流失" value="已流失" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 客户意向 -->
    <el-card class="info-card" header="客户意向" style="margin-top: 20px">
      <el-form :model="customerForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="意向产品">
              <el-select v-model="customerForm.intentProduct" placeholder="请选择意向产品" style="width: 100%">
                <el-option label="产品A" value="productA" />
                <el-option label="产品B" value="productB" />
                <el-option label="产品C" value="productC" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="意向度">
              <el-select v-model="customerForm.intentLevel" placeholder="请选择意向度" style="width: 100%">
                <el-option label="高" value="高" />
                <el-option label="中" value="中" />
                <el-option label="低" value="低" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算">
              <el-input v-model="customerForm.budget" placeholder="请输入预算" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预计成交时间">
              <el-date-picker
                v-model="customerForm.expectedTime"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 需求信息 -->
    <el-card class="info-card" header="需求信息" style="margin-top: 20px">
      <div class="requirement-header">
        <el-button type="primary" size="small" @click="handleAddRequirement">添加需求</el-button>
      </div>
      <el-table :data="customerForm.requirements" style="width: 100%">
        <el-table-column prop="content" label="需求内容">
          <template #default="scope">
            <el-input v-model="scope.row.content" placeholder="请输入需求内容" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleRemoveRequirement(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 跟进日志 -->
    <el-card class="info-card" header="跟进日志" style="margin-top: 20px">
      <div class="log-header">
        <el-button type="primary" size="small" @click="showLogDialog = true">添加日志</el-button>
      </div>
      <el-table :data="customerForm.followLogs" style="width: 100%">
        <el-table-column prop="content" label="日志内容">
          <template #default="scope">
            <el-input v-model="scope.row.content" placeholder="请输入日志内容" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleRemoveLog(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
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
import { ref, reactive, watch } from 'vue'
import ActionButtons from './ActionButtons.vue'

// 定义属性
const props = defineProps({
  userRole: {
    type: String,
    default: 'maker'
  },
  isSubmitted: {
    type: Boolean,
    default: false
  },
  customerData: {
    type: Object,
    default: () => ({
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
  }
})

// 定义事件
const emit = defineEmits([
  'save',
  'submit',
  'cancel',
  'reject',
  'return',
  'approve'
])

// 客户表单
const customerForm = reactive({ ...props.customerData })

// 监听customerData变化
watch(() => props.customerData, (newVal) => {
  Object.assign(customerForm, newVal)
}, { deep: true })

// 日志弹窗
const showLogDialog = ref(false)
const logForm = reactive({
  content: ''
})

// 处理保存
const handleSave = () => {
  emit('save', customerForm)
}

// 处理提交
const handleSubmit = () => {
  emit('submit', customerForm)
}

// 处理取消
const handleCancel = () => {
  emit('cancel')
}

// 处理拒绝
const handleReject = () => {
  emit('reject')
}

// 处理返回
const handleReturn = () => {
  emit('return')
}

// 处理批准
const handleApprove = () => {
  emit('approve')
}

// 添加需求
const handleAddRequirement = () => {
  customerForm.requirements.push({
    id: Date.now().toString(),
    content: '',
    createTime: new Date().toLocaleString()
  })
}

// 删除需求
const handleRemoveRequirement = (index: number) => {
  customerForm.requirements.splice(index, 1)
}

// 添加日志
const handleAddLog = () => {
  if (logForm.content.trim()) {
    customerForm.followLogs.push({
      id: Date.now().toString(),
      content: logForm.content,
      creator: '当前用户',
      createTime: new Date().toLocaleString()
    })
    logForm.content = ''
    showLogDialog.value = false
  }
}

// 删除日志
const handleRemoveLog = (index: number) => {
  customerForm.followLogs.splice(index, 1)
}
</script>

<style scoped>
.customer-edit-form {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
}

.left-buttons {
  display: flex;
  gap: 10px;
}

.info-card {
  margin-bottom: 20px;
}

.requirement-header,
.log-header {
  margin-bottom: 15px;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>