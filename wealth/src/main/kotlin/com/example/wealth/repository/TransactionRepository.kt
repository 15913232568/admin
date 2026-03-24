package com.example.wealth.repository

import com.example.wealth.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    
    fun findByAccountId(accountId: Long): List<Transaction>
    
    fun findByTransactionId(transactionId: String): Transaction?
    
    fun findByAccountIdAndTransactionDateBetween(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Transaction>
    
    fun findByAccountIdAndTransactionType(accountId: Long, transactionType: String): List<Transaction>
}