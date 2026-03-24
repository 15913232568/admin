package com.example.wealth.service

import com.example.wealth.entity.Transaction
import java.time.LocalDateTime

interface TransactionService {
    
    fun findAll(): List<Transaction>
    
    fun findById(id: Long): Transaction?
    
    fun findByTransactionId(transactionId: String): Transaction?
    
    fun findByAccountId(accountId: Long): List<Transaction>
    
    fun findByAccountIdAndDateRange(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Transaction>
    
    fun findByAccountIdAndTransactionType(
        accountId: Long,
        transactionType: String
    ): List<Transaction>
}