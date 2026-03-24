<template>
  <div class="action-buttons">
    <!-- Maker角色按钮 -->
    <template v-if="userRole === 'maker' && !isSubmitted">
      <el-button type="primary" @click="handleSave">保存</el-button>
      <el-button type="success" @click="handleSubmit">提交</el-button>
    </template>

    <!-- Checker角色按钮 -->
    <template v-if="userRole === 'checker'">
      <el-button type="danger" @click="handleReject">拒绝</el-button>
      <el-button @click="handleReturn">返回</el-button>
      <el-button type="success" @click="handleApprove">Approve</el-button>
    </template>
  </div>
</template>

<script setup lang="ts">


// 定义属性
const props = defineProps({
  userRole: {
    type: String,
    required: true,
    validator: (value: string) => ['maker', 'checker'].includes(value)
  },
  isSubmitted: {
    type: Boolean,
    default: false
  }
})

// 定义事件
const emit = defineEmits([
  'save',
  'submit',
  'reject',
  'return',
  'approve'
])

// 处理保存
const handleSave = () => {
  emit('save')
}

// 处理提交
const handleSubmit = () => {
  emit('submit')
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
</script>

<style scoped>
.action-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-bottom: 20px;
}
</style>