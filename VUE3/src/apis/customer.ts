
import type { CustomerFilter, CustomerListResponse, CustomerDetailResponse } from '../types/customer'

// 获取客户列表
export const getCustomerList = async (_params: CustomerFilter): Promise<CustomerListResponse> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success',
    data: [
      {
        id: '1',
        customerId: 'C001',
        cid: 'CID001',
        name: '张三',
        contact: '13800138001',
        source: '线上',
        owner: '李四',
        submitter: '王五',
        createTime: '2024-01-01 10:00:00',
        status: '超期',
        province: '北京',
        intentProduct: '产品A',
        intentLevel: '高',
        budget: '100万',
        expectedTime: '2024-03-01',
        requirements: [
          { id: '1', content: '需求1', createTime: '2024-01-01 10:00:00' },
          { id: '2', content: '需求2', createTime: '2024-01-02 10:00:00' }
        ],
        followLogs: [
          { id: '1', content: '跟进记录1', creator: '李四', createTime: '2024-01-01 10:00:00' },
          { id: '2', content: '跟进记录2', creator: '李四', createTime: '2024-01-02 10:00:00' }
        ]
      },
      {
        id: '2',
        customerId: 'C002',
        cid: 'CID002',
        name: '李四',
        contact: '13800138002',
        source: '线下',
        owner: '张三',
        submitter: '赵六',
        createTime: '2024-01-02 10:00:00',
        status: '正常',
        province: '上海',
        intentProduct: '产品B',
        intentLevel: '中',
        budget: '50万',
        expectedTime: '2024-04-01',
        requirements: [
          { id: '3', content: '需求3', createTime: '2024-01-02 10:00:00' }
        ],
        followLogs: [
          { id: '3', content: '跟进记录3', creator: '张三', createTime: '2024-01-02 10:00:00' }
        ]
      }
    ],
    total: 2
  }
}

// 获取客户详情
export const getCustomerDetail = async (id: string): Promise<CustomerDetailResponse> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success',
    data: {
      id: id,
      customerId: 'C001',
      cid: 'CID001',
      name: '张三',
      contact: '13800138001',
      source: '线上',
      owner: '李四',
      submitter: '王五',
      createTime: '2024-01-01 10:00:00',
      status: '超期',
      province: '北京',
      intentProduct: '产品A',
      intentLevel: '高',
      budget: '100万',
      expectedTime: '2024-03-01',
      requirements: [
        { id: '1', content: '需求1', createTime: '2024-01-01 10:00:00' },
        { id: '2', content: '需求2', createTime: '2024-01-02 10:00:00' }
      ],
      followLogs: [
        { id: '1', content: '跟进记录1', creator: '李四', createTime: '2024-01-01 10:00:00' },
        { id: '2', content: '跟进记录2', creator: '李四', createTime: '2024-01-02 10:00:00' }
      ]
    }
  }
}

// 批量释放客户
export const batchReleaseCustomers = async (_ids: string[]): Promise<any> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success'
  }
}

// 批量转派客户
export const batchAssignCustomers = async (_ids: string[], _owner: string): Promise<any> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success'
  }
}

// 释放客户
export const releaseCustomer = async (_id: string): Promise<any> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success'
  }
}

// 转派客户
export const assignCustomer = async (_id: string, _owner: string): Promise<any> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success'
  }
}

// 添加跟进日志
export const addFollowLog = async (_customerId: string, _content: string): Promise<any> => {
  // 模拟数据
  return {
    code: 200,
    message: 'success'
  }
}