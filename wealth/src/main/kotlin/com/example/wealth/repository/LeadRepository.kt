package com.example.wealth.repository

import com.example.wealth.entity.Lead
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * 潜在客户数据访问层
 */
@Repository
interface LeadRepository : JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    
    /**
     * 根据客户编号查询
     */
    fun findByCid(cid: String): Lead?
    
    /**
     * 根据客户ID查询
     */
    fun findByCustomerId(customerId: String): Lead?
    
    /**
     * 根据客户姓名模糊查询
     */
    fun findByNameContaining(name: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据状态查询
     */
    fun findByStatus(status: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据负责人查询
     */
    fun findByOwner(owner: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据来源查询
     */
    fun findBySource(source: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据意向产品查询
     */
    fun findByIntentProduct(intentProduct: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据意向级别查询
     */
    fun findByIntentLevel(intentLevel: String, pageable: Pageable): Page<Lead>
    
    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT l FROM Lead l WHERE l.createdAt BETWEEN :startDate AND :endDate")
    fun findByCreatedAtBetween(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<Lead>
    
    /**
     * 根据多个条件组合查询
     */
    @Query("""
        SELECT l FROM Lead l WHERE 
        (:customerId IS NULL OR l.customerId = :customerId) AND
        (:cid IS NULL OR l.cid = :cid) AND
        (:name IS NULL OR l.name LIKE %:name%) AND
        (:status IS NULL OR l.status = :status) AND
        (:owner IS NULL OR l.owner = :owner) AND
        (:source IS NULL OR l.source = :source) AND
        (:intentProduct IS NULL OR l.intentProduct = :intentProduct) AND
        (:intentLevel IS NULL OR l.intentLevel = :intentLevel) AND
        (:startDate IS NULL OR l.createdAt >= :startDate) AND
        (:endDate IS NULL OR l.createdAt <= :endDate)
    """)
    fun findByMultipleConditions(
        @Param("customerId") customerId: String?,
        @Param("cid") cid: String?,
        @Param("name") name: String?,
        @Param("status") status: String?,
        @Param("owner") owner: String?,
        @Param("source") source: String?,
        @Param("intentProduct") intentProduct: String?,
        @Param("intentLevel") intentLevel: String?,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): Page<Lead>
    
    /**
     * 检查客户编号是否存在
     */
    fun existsByCid(cid: String): Boolean
    
    /**
     * 检查客户ID是否存在
     */
    fun existsByCustomerId(customerId: String): Boolean
}