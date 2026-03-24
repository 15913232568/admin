<template>
  <div class="delivery-list">
    <h2>销售交付</h2>
    <el-table :data="deliveryList" style="width: 100%">
      <el-table-column prop="id" label="交付ID" width="100" />
      <el-table-column prop="customerId" label="客户ID" width="120" />
      <el-table-column prop="customerName" label="客户名称" width="150" />
      <el-table-column prop="product" label="产品" width="150" />
      <el-table-column prop="deliveryTime" label="交付时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleDetail(scope.row.id)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 交付列表
const deliveryList = ref<any[]>([
  {
    id: '1',
    customerId: 'C001',
    customerName: '张三',
    product: '产品A',
    deliveryTime: '2024-03-01 10:00:00',
    status: '已完成'
  },
  {
    id: '2',
    customerId: 'C002',
    customerName: '李四',
    product: '产品B',
    deliveryTime: '2024-04-01 10:00:00',
    status: '进行中'
  }
])

// 处理详情
const handleDetail = (id: string) => {
  console.log('查看交付详情:', id)
}

// 获取状态标签类型
const getStatusType = (status: string) => {
  switch (status) {
    case '已完成':
      return 'success'
    case '进行中':
      return 'warning'
    case '未开始':
      return 'info'
    default:
      return 'info'
  }
}
</script>

<style scoped>
.delivery-list {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.delivery-list h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--text-color);
}
</style>