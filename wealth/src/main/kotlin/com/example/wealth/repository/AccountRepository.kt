package com.example.wealth.repository

import com.example.wealth.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    
    fun findByUserId(userId: Long): List<Account>
    
    fun findByAccountNumber(accountNumber: String): Account?
    
    fun findByUserIdAndIsActive(userId: Long, isActive: Boolean): List<Account>
    
    fun existsByAccountNumber(accountNumber: String): Boolean
}