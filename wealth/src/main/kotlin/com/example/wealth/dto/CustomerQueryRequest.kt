package com.example.wealth.dto

import java.time.LocalDate

/**
 * 客户查询请求DTO
 */
data class CustomerQueryRequest(
    // 查询条件
    val customerName: String? = null,
    val phone: String? = null,
    val status: Int? = null,
    val createTimeStart: LocalDate? = null,
    val createTimeEnd: LocalDate? = null,
    
    // 分页参数
    val pageNum: Int = 1,
    val pageSize: Int = 10,
    val sortField: String = "createTime",
    val sortDir: String = "desc"
)