package com.example.wealth.service

import com.example.wealth.entity.Account
import java.math.BigDecimal

interface AccountService {
    
    fun findAll(): List<Account>
    
    fun findById(id: Long): Account?
    
    fun findByUserId(userId: Long): List<Account>
    
    fun findByAccountNumber(accountNumber: String): Account?
    
    fun create(account: Account): Account
    
    fun update(id: Long, account: Account): Account
    
    fun delete(id: Long)
    
    fun deposit(accountId: Long, amount: BigDecimal): Account
    
    fun withdraw(accountId: Long, amount: BigDecimal): Account
    
    fun transfer(fromAccountId: Long, toAccountId: Long, amount: BigDecimal)
}