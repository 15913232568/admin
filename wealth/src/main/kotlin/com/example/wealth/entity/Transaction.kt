package com.example.wealth.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "transactions")
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false, unique = true)
    var transactionId: String? = null,
    
    @Column(nullable = false)
    var amount: BigDecimal,
    
    @Column(nullable = false)
    var transactionType: String, // DEPOSIT, WITHDRAWAL, TRANSFER
    
    var description: String? = null,
    
    @Column(nullable = false)
    var status: String = "COMPLETED",
    
    @Column(name = "transaction_date", nullable = false)
    var transactionDate: LocalDateTime = LocalDateTime.now(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    var account: Account
) {
    
    @PrePersist
    fun generateTransactionId() {
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString()
        }
    }
}