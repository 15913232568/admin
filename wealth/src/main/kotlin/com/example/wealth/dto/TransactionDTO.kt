package com.example.wealth.dto

import java.math.BigDecimal
import java.time.LocalDateTime

// 交易DTO类，用于API响应
class TransactionDTO(
    val id: Long,
    val transactionId: String,
    val amount: BigDecimal,
    val transactionType: String,
    val description: String?,
    val status: String,
    val transactionDate: LocalDateTime,
    val accountId: Long
)

// 交易查询请求DTO
class TransactionQueryRequest(
    val accountId: Long,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val transactionType: String?
)

// 交易历史响应DTO
class TransactionHistoryDTO(
    val transactions: List<TransactionDTO>,
    val totalCount: Int,
    val page: Int,
    val pageSize: Int
)