package com.example.wealth.service

import com.example.wealth.entity.User

interface UserService {
    
    fun findAll(): List<User>
    
    fun findById(id: Long): User?
    
    fun findByUsername(username: String): User?
    
    fun findByEmail(email: String): User?
    
    fun create(user: User): User
    
    fun update(id: Long, user: User): User
    
    fun delete(id: Long)
    
    fun existsByUsername(username: String): Boolean
    
    fun existsByEmail(email: String): Boolean


}