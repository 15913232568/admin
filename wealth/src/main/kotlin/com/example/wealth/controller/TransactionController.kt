package com.example.wealth.controller

import com.example.wealth.entity.Transaction
import com.example.wealth.service.TransactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/transactions")
class TransactionController {
    
    @Autowired
    private lateinit var transactionService: TransactionService
    
    @Operation(summary = "获取所有交易", description = "返回系统中的所有交易记录")
    @ApiResponse(responseCode = "200", description = "成功获取交易记录列表")
    @GetMapping
    fun getAllTransactions(): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(transactionService.findAll())
    }
    
    @Operation(summary = "根据ID获取交易", description = "通过交易ID返回特定交易信息")
    @ApiResponse(responseCode = "200", description = "成功获取交易")
    @ApiResponse(responseCode = "404", description = "交易不存在")
    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable id: Long): ResponseEntity<Transaction> {
        val transaction = transactionService.findById(id)
        return if (transaction != null) {
            ResponseEntity.ok(transaction)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "根据交易流水号获取交易", description = "通过交易流水号返回特定交易记录")
    @ApiResponse(responseCode = "200", description = "成功获取交易记录")
    @ApiResponse(responseCode = "404", description = "交易记录不存在")
    @GetMapping("/transaction/{transactionId}")
    fun getTransactionByTransactionId(@PathVariable transactionId: String): ResponseEntity<Transaction> {
        val transaction = transactionService.findByTransactionId(transactionId)
        return if (transaction != null) {
            ResponseEntity.ok(transaction)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "根据账户ID获取交易记录", description = "返回指定账户的所有交易记录")
    @ApiResponse(responseCode = "200", description = "成功获取账户交易记录")
    @GetMapping("/account/{accountId}")
    fun getTransactionsByAccountId(@PathVariable accountId: Long): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(transactionService.findByAccountId(accountId))
    }
    
    @Operation(summary = "根据账户ID和日期范围获取交易记录", description = "返回指定账户在特定日期范围内的交易记录")
    @ApiResponse(responseCode = "200", description = "成功获取交易记录")
    @GetMapping("/account/{accountId}/date-range")
    fun getTransactionsByAccountIdAndDateRange(
        @PathVariable accountId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(
            transactionService.findByAccountIdAndDateRange(accountId, startDate, endDate)
        )
    }
    
    @Operation(summary = "根据账户ID和交易类型获取交易记录", description = "返回指定账户的特定类型交易记录")
    @ApiResponse(responseCode = "200", description = "成功获取交易记录")
    @GetMapping("/account/{accountId}/type/{transactionType}")
    fun getTransactionsByAccountIdAndType(
        @PathVariable accountId: Long,
        @PathVariable transactionType: String
    ): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(
            transactionService.findByAccountIdAndTransactionType(accountId, transactionType)
        )
    }
}