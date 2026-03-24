package com.example.wealth.controller

import com.example.wealth.entity.Account
import com.example.wealth.service.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts")
class AccountController {
    
    @Autowired
    private lateinit var accountService: AccountService
    
    @Operation(summary = "获取所有账户", description = "返回系统中的所有账户列表")
    @ApiResponse(responseCode = "200", description = "成功获取账户列表")
    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        return ResponseEntity.ok(accountService.findAll())
    }
    
    @Operation(summary = "根据ID获取账户", description = "通过账户ID返回特定账户信息")
    @ApiResponse(responseCode = "200", description = "成功获取账户")
    @ApiResponse(responseCode = "404", description = "账户不存在")
    @GetMapping("/{id}")
    fun getAccountById(@PathVariable id: Long): ResponseEntity<Account> {
        val account = accountService.findById(id)
        return if (account != null) {
            ResponseEntity.ok(account)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "根据用户ID获取账户", description = "返回指定用户的所有账户")
    @ApiResponse(responseCode = "200", description = "成功获取用户账户列表")
    @GetMapping("/user/{userId}")
    fun getAccountsByUserId(@PathVariable userId: Long): ResponseEntity<List<Account>> {
        return ResponseEntity.ok(accountService.findByUserId(userId))
    }
    
    @Operation(summary = "创建新账户", description = "为用户创建一个新的账户")
    @ApiResponse(responseCode = "201", description = "账户创建成功")
    @ApiResponse(responseCode = "400", description = "请求参数无效")
    @PostMapping
    fun createAccount(@RequestBody account: Account): ResponseEntity<Account> {
        val createdAccount = accountService.create(account)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }
    
    @Operation(summary = "更新账户信息", description = "更新指定ID的账户信息")
    @ApiResponse(responseCode = "200", description = "账户更新成功")
    @ApiResponse(responseCode = "404", description = "账户不存在")
    @PutMapping("/{id}")
    fun updateAccount(@PathVariable id: Long, @RequestBody account: Account): ResponseEntity<Account> {
        return try {
            ResponseEntity.ok(accountService.update(id, account))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "存款", description = "向指定账户存入资金")
    @ApiResponse(responseCode = "200", description = "存款成功")
    @ApiResponse(responseCode = "400", description = "无效的存款金额")
    @ApiResponse(responseCode = "404", description = "账户不存在")
    @PostMapping("/{id}/deposit")
    fun deposit(@PathVariable id: Long, @RequestBody request: DepositRequest): ResponseEntity<Account> {
        return try {
            ResponseEntity.ok(accountService.deposit(id, request.amount))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        }
    }
    
    @Operation(summary = "取款", description = "从指定账户取出资金")
    @ApiResponse(responseCode = "200", description = "取款成功")
    @ApiResponse(responseCode = "400", description = "无效的取款金额或余额不足")
    @ApiResponse(responseCode = "404", description = "账户不存在")
    @PostMapping("/{id}/withdraw")
    fun withdraw(@PathVariable id: Long, @RequestBody request: WithdrawRequest): ResponseEntity<Account> {
        return try {
            ResponseEntity.ok(accountService.withdraw(id, request.amount))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        }
    }
    
    @Operation(summary = "转账", description = "从一个账户向另一个账户转账")
    @ApiResponse(responseCode = "200", description = "转账成功")
    @ApiResponse(responseCode = "400", description = "转账失败")
    @ApiResponse(responseCode = "404", description = "账户不存在")
    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest): ResponseEntity<String> {
        return try {
            accountService.transfer(request.fromAccountId, request.toAccountId, request.amount)
            ResponseEntity.ok("Transfer successful")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
    
    // 请求模型类
    data class DepositRequest(val amount: BigDecimal)
    data class WithdrawRequest(val amount: BigDecimal)
    data class TransferRequest(val fromAccountId: Long, val toAccountId: Long, val amount: BigDecimal)
}