package com.example.wealth.dto

import jakarta.validation.constraints.NotBlank

/**
 * 登录请求DTO
 */
data class LoginRequestDTO(
    @field:NotBlank(message = "用户名不能为空")
    val username: String,
    @field:NotBlank(message = "密码不能为空")
    val password: String
)

/**
 * 登录响应DTO
 */
data class LoginResponseDTO(
    val token: String,
    val user: AuthUserDTO
)

/**
 * 注册请求DTO
 */
data class RegisterRequestDTO(
    val username: String,
    val password: String,
    val email: String,
    val fullName: String
)

/**
 * 认证用户DTO
 */
data class AuthUserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val roles: List<String>
)

/**
 * 统一响应体
 */
data class ResponseDTO<T>(
    val code: Int,
    val message: String,
    val data: T?
) {
    companion object {
        /**
         * 成功响应
         */
        fun <T> success(data: T): ResponseDTO<T> {
            return ResponseDTO(200, "success", data)
        }
        
        /**
         * 成功响应（无数据）
         */
        fun success(): ResponseDTO<Void> {
            return ResponseDTO(200, "success", null)
        }
        
        /**
         * 错误响应
         */
        fun <T> error(message: String): ResponseDTO<T> {
            return ResponseDTO(400, message, null)
        }
        
        /**
         * 未授权响应
         */
        fun <T> unauthorized(): ResponseDTO<T> {
            return ResponseDTO(401, "unauthorized", null)
        }
        
        /**
         * 服务器错误响应
         */
        fun <T> serverError(): ResponseDTO<T> {
            return ResponseDTO(500, "server error", null)
        }
    }
}