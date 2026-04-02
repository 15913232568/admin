package com.example.wealth.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.example.wealth.utils.JwtUtil

/**
 * JWT Token 验证过滤器（每次请求执行一次）
 */
@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService, // 自定义用户信息服务（需实现）
    private val jwtUtil: JwtUtil // JWT工具类（生成/验证Token，后续实现）
) : OncePerRequestFilter() {

    /**
     * 核心过滤逻辑
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. 跳过登录接口（无需验证Token）
        if (request.requestURI == "/api/auth/login") {
            filterChain.doFilter(request, response)
            return
        }

        // 2. 从请求头获取Token（通常格式：Bearer <token>）
        val authHeader = request.getHeader("Authorization")
        val jwtToken = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        // 3. Token为空 → 直接放行（后续Security会判定为未认证，返回401）
        if (jwtToken.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        // 4. 验证Token有效性并获取用户名
        val username = jwtUtil.extractUsername(jwtToken)
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            // 5. 加载用户信息
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)

            // 6. 验证Token是否有效
            if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
                // 7. 设置认证信息到Security上下文
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        // 8. 继续执行过滤器链
        filterChain.doFilter(request, response)
    }
}