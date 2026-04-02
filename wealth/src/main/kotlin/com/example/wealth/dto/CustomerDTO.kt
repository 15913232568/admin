package com.example.wealth.dto

import com.example.wealth.entity.Customer
import java.time.LocalDateTime

/**
 * 客户DTO
 */
data class CustomerDTO(
    val id: Long,
    val customerName: String,
    val phone: String,
    val status: Int,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime
) {
    companion object {
        /**
         * 从实体类创建DTO
         */
        fun fromEntity(customer: Customer): CustomerDTO {
            return CustomerDTO(
                id = customer.id!!,
                customerName = customer.customerName,
                phone = customer.phone,
                status = customer.status,
                createTime = customer.createTime,
                updateTime = customer.updateTime
            )
        }
    }
}