<template>
  <div class="customer-list">
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="客户ID">
          <el-input v-model="filterForm.customerId" placeholder="请输入客户ID" style="width: 150px" />
        </el-form-item>
        <el-form-item label="CID">
          <el-input v-model="filterForm.cid" placeholder="请输入CID" style="width: 150px" />
        </el-form-item>
        <el-form-item label="归属人">
          <el-select v-model="filterForm.owner" placeholder="请选择归属人" style="width: 150px">
            <el-option label="张三" value="zhangsan" />
            <el-option label="李四" value="lisi" />
          </el-select>
        </el-form-item>
        <el-form-item label="提交人">
          <el-select v-model="filterForm.submitter" placeholder="请选择提交人" style="width: 150px">
            <el-option label="王五" value="wangwu" />
            <el-option label="赵六" value="zhaoliu" />
          </el-select>
        </el-form-item>
        <el-form-item label="省份">
          <el-select v-model="filterForm.province" placeholder="请选择省份" style="width: 150px">
            <el-option label="北京" value="beijing" />
            <el-option label="上海" value="shanghai" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="filterForm.createTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 250px"
          />
        </el-form-item>
        <el-form-item label="意向产品">
          <el-select v-model="filterForm.intentProduct" placeholder="请选择意向产品" style="width: 150px">
            <el-option label="产品A" value="productA" />
            <el-option label="产品B" value="productB" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" plain @click="handleBatchRelease" :disabled="selectedIds.length === 0">
        批量释放
      </el-button>
      <el-button type="primary" plain @click="handleBatchAssign" :disabled="selectedIds.length === 0">
        批量转派
      </el-button>
    </div>

    <!-- 客户表格 -->
    <el-table
      v-loading="loading"
      :data="customerList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="customerId" label="客户ID" width="120" />
      <el-table-column prop="province" label="省份" width="100" />
      <el-table-column prop="intentProduct" label="意向产品" width="150" />
      <el-table-column prop="intentLevel" label="意向度" width="100">
        <template #default="scope">
          <el-tag :type="getIntentLevelType(scope.row.intentLevel)">{{ scope.row.intentLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="需求信息" label="需求信息" width="200">
        <template #default="scope">
          <span>{{ scope.row.requirements?.length || 0 }} 条需求</span>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column prop="owner" label="归属人" width="100" />
      <el-table-column prop="submitter" label="提交人" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <span v-if="scope.row.status === 'overdue'" style="color: red">超期</span>
          <span v-else>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleDetail(scope.row.id)">详情</el-button>
          <el-button size="small" @click="handleRelease(scope.row.id)">释放</el-button>
          <el-button size="small" @click="handleAssign(scope.row.id)">转派</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCustomerList } from '../../apis/customer'

// 路由
const router = useRouter()

// 筛选参数
const filterForm = ref({
  customerId: '',
  cid: '',
  owner: '',
  submitter: '',
  province: '',
  createTime: ['', ''] as [string, string],
  intentProduct: ''
})

// 分页参数
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 选中的客户ID
const selectedIds = ref<string[]>([])

// 加载状态
const loading = ref(false)

// 客户列表
const customerList = ref<any[]>([])

// 处理搜索
const handleSearch = async () => {
  loading.value = true
  try {
    const response = await getCustomerList({
      ...filterForm.value,
      page: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    })
    customerList.value = response.data
    pagination.value.total = response.total
  } catch (error) {
    console.error('获取客户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置筛选
const resetFilter = () => {
  filterForm.value = {
    customerId: '',
    cid: '',
    owner: '',
    submitter: '',
    province: '',
    createTime: ['', ''] as [string, string],
    intentProduct: ''
  }
  handleSearch()
}

// 处理选择变化
const handleSelectionChange = (val: any[]) => {
  selectedIds.value = val.map(item => item.id)
}

// 处理批量释放
const handleBatchRelease = () => {
  // 批量释放逻辑
  console.log('批量释放:', selectedIds.value)
}

// 处理批量转派
const handleBatchAssign = () => {
  // 批量转派逻辑
  console.log('批量转派:', selectedIds.value)
}

// 处理详情
const handleDetail = (id: string) => {
  router.push(`/customer/detail/${id}`)
}

// 处理释放
const handleRelease = (id: string) => {
  // 释放逻辑
  console.log('释放:', id)
}

// 处理转派
const handleAssign = (id: string) => {
  // 转派逻辑
  console.log('转派:', id)
}

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  handleSearch()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  pagination.value.currentPage = current
  handleSearch()
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
  handleSearch()
})
</script>

<style scoped>
.customer-list {
  background-color: var(--white);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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