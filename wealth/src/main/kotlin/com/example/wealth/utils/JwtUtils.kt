package com.example.wealth.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtUtil(
    // 从配置文件读取JWT密钥和过期时间（建议配置在application.yml）
    @Value("\${jwt.secret:your-secret-key-32bytes-long-1234567890}")
    private val secret: String,
    @Value("\${jwt.expiration:86400000}") // 过期时间：24小时（毫秒）
    private val expiration: Long
) {

    // 生成JWT密钥（需至少32位）
    private val signingKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    /**
     * 从Token中提取用户名
     */
    fun extractUsername(token: String): String = extractAllClaims(token).subject

    /**
     * 生成Token（基于用户信息）
     */
    fun generateToken(userDetails: UserDetails): String =
        Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(signingKey)
            .compact()

    /**
     * 验证Token是否有效（用户名匹配 + 未过期）
     */
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    /**
     * 检查Token是否过期
     */
    private fun isTokenExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date())

    /**
     * 提取Token中的所有Claims
     */
    private fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
}