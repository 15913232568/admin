package com.example.wealth.service.impl

import com.example.wealth.entity.Transaction
import com.example.wealth.repository.TransactionRepository
import com.example.wealth.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionServiceImpl : TransactionService {
    
    @Autowired
    private lateinit var transactionRepository: TransactionRepository
    
    override fun findAll(): List<Transaction> {
        return transactionRepository.findAll().toList()
    }
    
    override fun findById(id: Long): Transaction? {
        return transactionRepository.findById(id).orElse(null)
    }
    
    override fun findByTransactionId(transactionId: String): Transaction? {
        return transactionRepository.findByTransactionId(transactionId)
    }
    
    override fun findByAccountId(accountId: Long): List<Transaction> {
        return transactionRepository.findByAccountId(accountId).toList()
    }
    
    override fun findByAccountIdAndDateRange(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Transaction> {
        return transactionRepository.findByAccountIdAndTransactionDateBetween(accountId, startDate, endDate).toList()
    }
    
    override fun findByAccountIdAndTransactionType(
        accountId: Long,
        transactionType: String
    ): List<Transaction> {
        return transactionRepository.findByAccountIdAndTransactionType(accountId, transactionType).toList()
    }
}