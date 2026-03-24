// 客户列表筛选参数
export interface CustomerFilter {
  customerId?: string
  cid?: string
  owner?: string
  submitter?: string
  province?: string
  createTime?: [string, string]
  intentProduct?: string
  page?: number
  pageSize?: number
}

// 客户状态
export type CustomerStatus = '正常' | '超期' | '已释放'

// 客户意向度
export type IntentLevel = '高' | '中' | '低'

// 客户需求
export interface CustomerRequirement {
  id: string
  content: string
  createTime: string
}

// 跟进日志
export interface FollowLog {
  id: string
  content: string
  creator: string
  createTime: string
}

// 客户信息
export interface Customer {
  id: string
  customerId: string
  cid: string
  name: string
  contact: string
  source: string
  owner: string
  submitter: string
  createTime: string
  status: CustomerStatus
  province: string
  intentProduct: string
  intentLevel: IntentLevel
  budget?: string
  expectedTime?: string
  requirements: CustomerRequirement[]
  followLogs: FollowLog[]
}

// 客户列表响应
export interface CustomerListResponse {
  code: number
  message: string
  data: Customer[]
  total: number
}

// 客户详情响应
export interface CustomerDetailResponse {
  code: number
  message: string
  data: Customer
}