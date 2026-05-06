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
        <el-form-item label="客户名称">
          <el-input v-model="filterForm.name" placeholder="请输入客户名称" style="width: 150px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" style="width: 150px">
            <el-option label="待跟进" value="pending" />
            <el-option label="跟进中" value="following" />
            <el-option label="已提交" value="submitted" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item label="归属人">
          <el-select v-model="filterForm.owner" placeholder="请选择归属人" style="width: 150px">
            <el-option label="张三" value="zhangsan" />
            <el-option label="李四" value="lisi" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户来源">
          <el-select v-model="filterForm.source" placeholder="请选择来源" style="width: 150px">
            <el-option label="线上" value="online" />
            <el-option label="线下" value="offline" />
            <el-option label="推荐" value="recommend" />
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
        <el-form-item label="意向产品">
          <el-select v-model="filterForm.intentProduct" placeholder="请选择意向产品" style="width: 150px">
            <el-option label="产品A" value="productA" />
            <el-option label="产品B" value="productB" />
          </el-select>
        </el-form-item>
        <el-form-item label="意向度">
          <el-select v-model="filterForm.intentLevel" placeholder="请选择意向度" style="width: 150px">
            <el-option label="高" value="high" />
            <el-option label="中" value="medium" />
            <el-option label="低" value="low" />
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
      <el-table-column prop="requirements" label="需求信息" width="200">
        <template #default="scope">
          <span>{{ scope.row.requirements?.length || 0 }} 条需求</span>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column prop="owner" label="归属人" width="100" />
      <el-table-column prop="submitter" label="提交人" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
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
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

// 路由
const router = useRouter()

// 筛选参数 - 根据后端LeadQueryRequest模型
const filterForm = ref({
  customerId: '',
  cid: '',
  name: '',
  status: '',
  owner: '',
  source: '',
  intentProduct: '',
  intentLevel: '',
  startDate: '',
  endDate: '',
  submitter: '',
  province: '',
  createTime: ['', ''] as [string, string]
})

// 分页参数
const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0,
  totalPages: 0
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
    // 构建查询参数 - 根据后端LeadQueryRequest模型
    const queryParams: any = {
      pageNum: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      sortField: 'createdAt',
      sortDir: 'desc'
    }
    
    // 处理日期范围 - 将createTime转换为startDate和endDate
    if (filterForm.value.createTime && filterForm.value.createTime.length === 2) {
      const [startDate, endDate] = filterForm.value.createTime
      if (startDate) {
        queryParams.startDate = startDate
      }
      if (endDate) {
        queryParams.endDate = endDate
      }
    }
    
    // 只添加非空字符串的查询条件（排除createTime，因为它已经处理过了）
    Object.keys(filterForm.value).forEach(key => {
      if (key === 'createTime') return // 跳过已处理的日期范围
      
      const value = (filterForm.value as any)[key]
      // 只有当值不为空字符串时才添加
      if (value !== '' && value !== null && value !== undefined) {
        queryParams[key] = value
      }
    })
    
    console.log('发送的查询参数:', queryParams)
    
    // 调用后端API - 客户管理页面使用/leads/list
    const response: any = await request.post('/leads/list', queryParams)
    
    // 检查响应格式，兼容不同的后端响应结构
    if (response.code === 200) {
      const data = response.data
      customerList.value = data.records || data.list || []
      pagination.value.total = data.total || 0
      pagination.value.totalPages = data.totalPages || Math.ceil(pagination.value.total / pagination.value.pageSize)
    } else {
      ElMessage.error(response.message || '获取客户列表失败')
    }
  } catch (error: any) {
    console.error('获取客户列表失败:', error)
    
    // 处理认证失败的情况
    if (error.code === 401) {
      ElMessage.warning('认证已过期，请重新登录')
      // 不自动跳转，让用户手动处理
    } else {
      ElMessage.error('获取客户列表失败，请检查网络连接')
      
      // 如果后端接口不可用，将列表设为空数组，不显示任何数据
      customerList.value = []
      pagination.value.total = 0
      pagination.value.totalPages = 0
    }
  } finally {
    loading.value = false
  }
}

// 重置筛选
const resetFilter = () => {
  filterForm.value = {
    customerId: '',
    cid: '',
    name: '',
    status: '',
    owner: '',
    source: '',
    intentProduct: '',
    intentLevel: '',
    startDate: '',
    endDate: '',
    submitter: '',
    province: '',
    createTime: ['', ''] as [string, string]
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