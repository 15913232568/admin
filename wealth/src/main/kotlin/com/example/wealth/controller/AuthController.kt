package com.example.wealth.controller

import com.example.wealth.utils.JwtUtil
import com.example.wealth.repository.UserRepository
import com.example.wealth.service.impl.UserDetailsServiceImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        // 1. 验证用户名密码
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        // 2. 加载用户信息并生成Token
        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        val jwtToken = jwtUtil.generateToken(userDetails)

        // 3. 返回Token
        return ResponseEntity.ok(LoginResponse(token = jwtToken, type = "Bearer"))
    }

    // 登录请求参数
    data class LoginRequest(val username: String, val password: String)

    // 登录响应
    data class LoginResponse(val token: String, val type: String)
}