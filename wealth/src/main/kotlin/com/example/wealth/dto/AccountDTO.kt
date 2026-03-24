package com.example.wealth.dto

import java.math.BigDecimal
import java.time.LocalDateTime

// 账户DTO类，用于API响应
class AccountDTO(
    val id: Long,
    val accountNumber: String,
    val accountType: String,
    val balance: BigDecimal,
    val currencyCode: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val userId: Long
)

// 账户创建请求DTO
class AccountCreateRequest(
    val accountType: String,
    val currencyCode: String = "USD",
    val userId: Long
)

// 账户更新请求DTO
class AccountUpdateRequest(
    val accountType: String,
    val currencyCode: String,
    val isActive: Boolean
)

// 账户余额响应DTO
class AccountBalanceDTO(
    val accountNumber: String,
    val balance: BigDecimal,
    val currencyCode: String
)