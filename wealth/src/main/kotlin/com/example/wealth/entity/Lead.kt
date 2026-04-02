package com.example.wealth.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 潜在客户实体类
 */
@Entity
@Table(name = "leads")
class Lead(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(name = "customer_id", nullable = false, length = 50)
    var customerId: String = "",
    
    @Column(nullable = false, unique = true, length = 50)
    var cid: String = "",
    
    @Column(nullable = false, length = 100)
    var name: String = "",
    
    @Column(length = 200)
    var contact: String? = null,
    
    @Column(length = 50)
    var source: String? = null,
    
    @Column(length = 50)
    var owner: String? = null,
    
    @Column(length = 50)
    var submitter: String? = null,
    
    @Column(length = 20)
    var status: String = "待跟进",
    
    @Column(name = "intent_product", length = 100)
    var intentProduct: String? = null,
    
    @Column(name = "intent_level", length = 10)
    var intentLevel: String = "中",
    
    @Column(precision = 15, scale = 2)
    var budget: BigDecimal? = null,
    
    @Column(name = "expected_time")
    var expectedTime: LocalDate? = null,
    
    @Column(columnDefinition = "JSON")
    var requirements: String? = null,
    
    @Column(name = "follow_logs", columnDefinition = "JSON")
    var followLogs: String? = null,
    
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    /**
     * 需求信息数据类
     */
    data class Requirement(
        val id: String,
        val type: String,
        val description: String,
        val priority: String = "中",
        val createdAt: LocalDateTime = LocalDateTime.now()
    )
    
    /**
     * 跟进日志数据类
     */
    data class FollowLog(
        val id: String,
        val content: String,
        val operator: String,
        val nextFollowTime: LocalDate? = null,
        val createdAt: LocalDateTime = LocalDateTime.now()
    )
    
    /**
     * 获取需求列表
     */
    fun getRequirementsList(): List<Requirement> {
        return if (requirements.isNullOrBlank()) {
            emptyList()
        } else {
            try {
                jacksonObjectMapper().readValue(requirements, Array<Requirement>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    /**
     * 设置需求列表
     */
    fun setRequirementsList(requirementsList: List<Requirement>) {
        this.requirements = if (requirementsList.isEmpty()) {
            null
        } else {
            jacksonObjectMapper().writeValueAsString(requirementsList)
        }
    }
    
    /**
     * 获取跟进日志列表
     */
    fun getFollowLogsList(): List<FollowLog> {
        return if (followLogs.isNullOrBlank()) {
            emptyList()
        } else {
            try {
                jacksonObjectMapper().readValue(followLogs, Array<FollowLog>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    /**
     * 设置跟进日志列表
     */
    fun setFollowLogsList(followLogsList: List<FollowLog>) {
        this.followLogs = if (followLogsList.isEmpty()) {
            null
        } else {
            jacksonObjectMapper().writeValueAsString(followLogsList)
        }
    }
    
    /**
     * 添加需求
     */
    fun addRequirement(requirement: Requirement) {
        val currentRequirements = getRequirementsList().toMutableList()
        currentRequirements.add(requirement)
        setRequirementsList(currentRequirements)
    }
    
    /**
     * 添加跟进日志
     */
    fun addFollowLog(followLog: FollowLog) {
        val currentLogs = getFollowLogsList().toMutableList()
        currentLogs.add(followLog)
        setFollowLogsList(currentLogs)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Lead
        
        if (id != other.id) return false
        if (cid != other.cid) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + cid.hashCode()
        return result
    }
    
    override fun toString(): String {
        return "Lead(id=$id, cid='$cid', name='$name', status='$status')"
    }
}