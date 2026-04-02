package com.example.wealth.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 客户实体类
 */
@Entity
@Table(name = "customer")
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(name = "customer_name", nullable = false)
    var customerName: String,
    
    @Column(name = "phone", nullable = false)
    var phone: String,
    
    @Column(name = "status", nullable = false)
    var status: Int,
    
    @Column(name = "create_time", nullable = false, updatable = false)
    val createTime: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "update_time", nullable = false)
    var updateTime: LocalDateTime = LocalDateTime.now()
) {
    /**
     * 更新时间自动设置
     */
    @PreUpdate
    fun preUpdate() {
        updateTime = LocalDateTime.now()
    }
}