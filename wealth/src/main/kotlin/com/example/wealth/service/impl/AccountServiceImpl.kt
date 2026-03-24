package com.example.wealth.service.impl

import com.example.wealth.entity.Account
import com.example.wealth.entity.Transaction
import com.example.wealth.repository.AccountRepository
import com.example.wealth.repository.TransactionRepository
import com.example.wealth.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Random

@Service
class AccountServiceImpl : AccountService {
    
    @Autowired
    private lateinit var accountRepository: AccountRepository
    
    @Autowired
    private lateinit var transactionRepository: TransactionRepository
    
    override fun findAll(): List<Account> {
        return accountRepository.findAll().toList()
    }
    
    override fun findById(id: Long): Account? {
        return accountRepository.findById(id).orElse(null)
    }
    
    override fun findByUserId(userId: Long): List<Account> {
        return accountRepository.findByUserId(userId).toList()
    }
    
    override fun findByAccountNumber(accountNumber: String): Account? {
        return accountRepository.findByAccountNumber(accountNumber)
    }
    
    @Transactional
    override fun create(account: Account): Account {
        // 生成账号
        if (account.accountNumber.isNullOrEmpty()) {
            account.accountNumber = generateAccountNumber()
        }
        
        // 检查账号是否已存在
        if (accountRepository.existsByAccountNumber(account.accountNumber)) {
            throw IllegalArgumentException("Account number already exists")
        }
        
        account.createdAt = LocalDateTime.now()
        account.updatedAt = LocalDateTime.now()
        
        return accountRepository.save(account)
    }
    
    @Transactional
    override fun update(id: Long, account: Account): Account {
        val existingAccount = accountRepository.findById(id).orElse(null)
            ?: throw IllegalArgumentException("Account not found with id: $id")
        
        existingAccount.accountType = account.accountType
        existingAccount.currencyCode = account.currencyCode
        existingAccount.isActive = account.isActive
        existingAccount.updatedAt = LocalDateTime.now()
        
        return accountRepository.save(existingAccount)
    }
    
    @Transactional
    override fun delete(id: Long) {
        if (!accountRepository.existsById(id)) {
            throw IllegalArgumentException("Account not found with id: $id")
        }
        accountRepository.deleteById(id)
    }
    
    @Transactional
    override fun deposit(accountId: Long, amount: BigDecimal): Account {
        val account = accountRepository.findById(accountId).orElse(null)
            ?: throw IllegalArgumentException("Account not found with id: $accountId")
        
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Deposit amount must be positive")
        }
        
        account.balance = account.balance.add(amount)
        account.updatedAt = LocalDateTime.now()
        
        // 创建交易记录
        val transaction = Transaction(
            amount = amount,
            transactionType = "DEPOSIT",
            description = "Deposit to account",
            account = account
        )
        transactionRepository.save(transaction)
        
        return accountRepository.save(account)
    }
    
    @Transactional
    override fun withdraw(accountId: Long, amount: BigDecimal): Account {
        val account = accountRepository.findById(accountId).orElse(null)
            ?: throw IllegalArgumentException("Account not found with id: $accountId")
        
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Withdrawal amount must be positive")
        }
        
        if (account.balance < amount) {
            throw IllegalArgumentException("Insufficient funds")
        }
        
        account.balance = account.balance.subtract(amount)
        account.updatedAt = LocalDateTime.now()
        
        // 创建交易记录
        val transaction = Transaction(
            amount = amount,
            transactionType = "WITHDRAWAL",
            description = "Withdrawal from account",
            account = account
        )
        transactionRepository.save(transaction)
        
        return accountRepository.save(account)
    }
    
    @Transactional
    override fun transfer(fromAccountId: Long, toAccountId: Long, amount: BigDecimal) {
        if (fromAccountId == toAccountId) {
            throw IllegalArgumentException("Cannot transfer to the same account")
        }
        
        val fromAccount = accountRepository.findById(fromAccountId).orElse(null)
            ?: throw IllegalArgumentException("From account not found")
        
        val toAccount = accountRepository.findById(toAccountId).orElse(null)
            ?: throw IllegalArgumentException("To account not found")
        
        // 检查转账金额
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Transfer amount must be positive")
        }
        
        // 检查余额
        if (fromAccount.balance < amount) {
            throw IllegalArgumentException("Insufficient funds")
        }
        
        // 执行转账
        fromAccount.balance = fromAccount.balance.subtract(amount)
        toAccount.balance = toAccount.balance.add(amount)
        
        fromAccount.updatedAt = LocalDateTime.now()
        toAccount.updatedAt = LocalDateTime.now()
        
        // 创建转出交易记录
        val fromTransaction = Transaction(
            amount = amount,
            transactionType = "TRANSFER_OUT",
            description = "Transfer to account ${toAccount.accountNumber}",
            account = fromAccount
        )
        
        // 创建转入交易记录
        val toTransaction = Transaction(
            amount = amount,
            transactionType = "TRANSFER_IN",
            description = "Transfer from account ${fromAccount.accountNumber}",
            account = toAccount
        )
        
        transactionRepository.save(fromTransaction)
        transactionRepository.save(toTransaction)
        
        accountRepository.save(fromAccount)
        accountRepository.save(toAccount)
    }
    
    private fun generateAccountNumber(): String {
        val random = Random()
        val sb = StringBuilder()
        // 生成16位账号
        repeat(16) {
            sb.append(random.nextInt(10))
        }
        return sb.toString()
    }
}