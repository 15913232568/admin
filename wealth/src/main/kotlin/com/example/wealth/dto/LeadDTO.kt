package com.example.wealth.dto

import com.example.wealth.entity.Lead
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 潜在客户创建请求DTO
 */
data class LeadCreateRequest(
    @field:NotBlank(message = "客户ID不能为空")
    val customerId: String,
    
    @field:NotBlank(message = "客户编号不能为空")
    val cid: String,
    
    @field:NotBlank(message = "客户姓名不能为空")
    val name: String,
    
    val contact: String? = null,
    val source: String? = null,
    val owner: String? = null,
    val submitter: String? = null,
    val status: String = "待跟进",
    val intentProduct: String? = null,
    val intentLevel: String = "中",
    val budget: BigDecimal? = null,
    val expectedTime: LocalDate? = null,
    val requirements: List<Lead.Requirement> = emptyList(),
    val followLogs: List<Lead.FollowLog> = emptyList()
)

/**
 * 潜在客户更新请求DTO
 */
data class LeadUpdateRequest(
    val customerId: String? = null,
    val cid: String? = null,
    val name: String? = null,
    val contact: String? = null,
    val source: String? = null,
    val owner: String? = null,
    val submitter: String? = null,
    val status: String? = null,
    val intentProduct: String? = null,
    val intentLevel: String? = null,
    val budget: BigDecimal? = null,
    val expectedTime: LocalDate? = null,
    val requirements: List<Lead.Requirement>? = null,
    val followLogs: List<Lead.FollowLog>? = null
)

/**
 * 潜在客户响应DTO
 */
data class LeadResponse(
    val id: Long,
    val customerId: String,
    val cid: String,
    val name: String,
    val contact: String?,
    val source: String?,
    val owner: String?,
    val submitter: String?,
    val status: String,
    val intentProduct: String?,
    val intentLevel: String,
    val budget: BigDecimal?,
    val expectedTime: LocalDate?,
    val requirements: List<Lead.Requirement>,
    val followLogs: List<Lead.FollowLog>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * 从实体转换为响应DTO
         */
        fun fromEntity(lead: Lead): LeadResponse {
            return LeadResponse(
                id = lead.id!!,
                customerId = lead.customerId,
                cid = lead.cid,
                name = lead.name,
                contact = lead.contact,
                source = lead.source,
                owner = lead.owner,
                submitter = lead.submitter,
                status = lead.status,
                intentProduct = lead.intentProduct,
                intentLevel = lead.intentLevel,
                budget = lead.budget,
                expectedTime = lead.expectedTime,
                requirements = lead.getRequirementsList(),
                followLogs = lead.getFollowLogsList(),
                createdAt = lead.createdAt,
                updatedAt = lead.updatedAt
            )
        }
    }
}

/**
 * 潜在客户查询DTO
 */
data class LeadQueryRequest(
    val customerId: String? = null,
    val cid: String? = null,
    val name: String? = null,
    val status: String? = null,
    val owner: String? = null,
    val source: String? = null,
    val intentProduct: String? = null,
    val intentLevel: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val pageNum: Int = 1,
    val pageSize: Int = 20,
    val sortField: String = "createdAt",
    val sortDir: String = "desc"
)

/**
 * 分页响应DTO
 */
data class PageResponse<T>(
    val total: Long,
    val pageNum: Int,
    val pageSize: Int,
    val totalPages: Int,
    val records: List<T>
)