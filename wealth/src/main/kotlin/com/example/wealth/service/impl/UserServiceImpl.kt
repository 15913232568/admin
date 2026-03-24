package com.example.wealth.service.impl

import com.example.wealth.entity.User
import com.example.wealth.repository.UserRepository
import com.example.wealth.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class UserServiceImpl : UserService {
    
    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    
    override fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }
    
    override fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }
    
    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
    
    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
    
    @Transactional
    override fun create(user: User): User {
        // 检查用户名和邮箱是否已存在
        if (existsByUsername(user.username)) {
            throw IllegalArgumentException("Username already exists")
        }
        if (existsByEmail(user.email)) {
            throw IllegalArgumentException("Email already exists")
        }
        
        // 加密密码
        user.password = passwordEncoder.encode(user.password)
        
        // 设置创建和更新时间
        user.createdAt = LocalDateTime.now()
        user.updatedAt = LocalDateTime.now()
        
        return userRepository.save(user)
    }
    
    @Transactional
    override fun update(id: Long, user: User): User {
        val existingUser = userRepository.findById(id).orElse(null)
            ?: throw IllegalArgumentException("User not found with id: $id")
        
        existingUser.username = user.username
        existingUser.email = user.email
        existingUser.fullName = user.fullName
        existingUser.password = passwordEncoder.encode(user.password)
        existingUser.updatedAt = LocalDateTime.now()
        
        return userRepository.save(existingUser)
    }
    
    @Transactional
    override fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw IllegalArgumentException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }
    
    override fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
    
    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}