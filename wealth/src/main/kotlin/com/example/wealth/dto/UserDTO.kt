package com.example.wealth.dto

import java.time.LocalDateTime

// 用户DTO类，用于API响应
class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

// 用户创建请求DTO
class UserCreateRequest(
    val username: String,
    val password: String,
    val email: String,
    val fullName: String
)

// 用户更新请求DTO
class UserUpdateRequest(
    val username: String,
    val email: String,
    val fullName: String,
    val password: String? = null
)