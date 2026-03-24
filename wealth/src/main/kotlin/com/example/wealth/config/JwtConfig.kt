package com.example.wealth.config

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.security.Key

@Configuration
class JwtConfig {
    
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    
    @Value("\${jwt.expiration}")
    private var expiration: Long = 3600000 // 默认1小时
    
    @Value("\${jwt.issuer}")
    private lateinit var issuer: String
    
    /**
     * 获取JWT密钥
     */
    fun getKey(): Key {
        return Keys.hmacShaKeyFor(secret.toByteArray())
    }
    
    /**
     * 获取过期时间
     */
    fun getExpiration(): Long {
        return expiration
    }
    
    /**
     * 获取签发者
     */
    fun getIssuer(): String {
        return issuer
    }
}