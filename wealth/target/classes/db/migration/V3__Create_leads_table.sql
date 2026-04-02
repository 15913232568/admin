-- 创建潜在客户表
CREATE TABLE leads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL COMMENT '客户ID',
    cid VARCHAR(50) NOT NULL UNIQUE COMMENT '客户编号',
    name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    contact VARCHAR(200) COMMENT '联系方式',
    source VARCHAR(50) COMMENT '客户来源',
    owner VARCHAR(50) COMMENT '负责人',
    submitter VARCHAR(50) COMMENT '提交人',
    status VARCHAR(20) DEFAULT '待跟进' COMMENT '状态',
    intent_product VARCHAR(100) COMMENT '意向产品',
    intent_level VARCHAR(10) DEFAULT '中' COMMENT '意向级别',
    budget DECIMAL(15,2) COMMENT '预算',
    expected_time DATE COMMENT '期望时间',
    
    -- 使用JSON数组存储需求信息
    requirements JSON COMMENT '需求信息',
    
    -- 使用JSON数组存储跟进日志
    follow_logs JSON COMMENT '跟进日志',
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_customer_id (customer_id),
    INDEX idx_cid (cid),
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_owner (owner),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='潜在客户表';