package com.example.wealth.service

import com.example.wealth.dto.LeadCreateRequest
import com.example.wealth.dto.LeadQueryRequest
import com.example.wealth.dto.LeadResponse
import com.example.wealth.dto.LeadUpdateRequest
import com.example.wealth.dto.PageResponse
import com.example.wealth.entity.Lead
import com.example.wealth.repository.LeadRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * 潜在客户业务逻辑层
 */
@Service
@Transactional
class LeadService(private val leadRepository: LeadRepository) {
    
    /**
     * 创建潜在客户
     */
    fun createLead(request: LeadCreateRequest): LeadResponse {
        // 检查客户编号是否已存在
        if (leadRepository.existsByCid(request.cid)) {
            throw IllegalArgumentException("客户编号已存在: ${request.cid}")
        }
        
        // 检查客户ID是否已存在
        if (leadRepository.existsByCustomerId(request.customerId)) {
            throw IllegalArgumentException("客户ID已存在: ${request.customerId}")
        }
        
        val lead = Lead(
            customerId = request.customerId,
            cid = request.cid,
            name = request.name,
            contact = request.contact,
            source = request.source,
            owner = request.owner,
            submitter = request.submitter,
            status = request.status,
            intentProduct = request.intentProduct,
            intentLevel = request.intentLevel,
            budget = request.budget,
            expectedTime = request.expectedTime
        )
        
        // 设置需求列表
        lead.setRequirementsList(request.requirements)
        
        // 设置跟进日志列表
        lead.setFollowLogsList(request.followLogs)
        
        val savedLead = leadRepository.save(lead)
        return LeadResponse.fromEntity(savedLead)
    }
    
    /**
     * 根据ID查询潜在客户
     */
    @Transactional(readOnly = true)
    fun getLeadById(id: Long): LeadResponse {
        val lead = leadRepository.findById(id)
            .orElseThrow { IllegalArgumentException("潜在客户不存在: $id") }
        return LeadResponse.fromEntity(lead)
    }
    
    /**
     * 根据客户编号查询潜在客户
     */
    @Transactional(readOnly = true)
    fun getLeadByCid(cid: String): LeadResponse {
        val lead = leadRepository.findByCid(cid)
            ?: throw IllegalArgumentException("潜在客户不存在: $cid")
        return LeadResponse.fromEntity(lead)
    }
    
    /**
     * 更新潜在客户
     */
    fun updateLead(id: Long, request: LeadUpdateRequest): LeadResponse {
        val lead = leadRepository.findById(id)
            .orElseThrow { IllegalArgumentException("潜在客户不存在: $id") }
        
        // 更新字段
        request.customerId?.let { lead.customerId = it }
        request.cid?.let { lead.cid = it }
        request.name?.let { lead.name = it }
        request.contact?.let { lead.contact = it }
        request.source?.let { lead.source = it }
        request.owner?.let { lead.owner = it }
        request.submitter?.let { lead.submitter = it }
        request.status?.let { lead.status = it }
        request.intentProduct?.let { lead.intentProduct = it }
        request.intentLevel?.let { lead.intentLevel = it }
        request.budget?.let { lead.budget = it }
        request.expectedTime?.let { lead.expectedTime = it }
        request.requirements?.let { lead.setRequirementsList(it) }
        request.followLogs?.let { lead.setFollowLogsList(it) }
        
        lead.updatedAt = java.time.LocalDateTime.now()
        
        val updatedLead = leadRepository.save(lead)
        return LeadResponse.fromEntity(updatedLead)
    }
    
    /**
     * 删除潜在客户
     */
    fun deleteLead(id: Long) {
        if (!leadRepository.existsById(id)) {
            throw IllegalArgumentException("潜在客户不存在: $id")
        }
        leadRepository.deleteById(id)
    }
    
    /**
     * 分页查询潜在客户列表 - 使用 Spring Data JPA Specification 实现
     */
    @Transactional(readOnly = true)
    fun getLeads(
        query: LeadQueryRequest,
        pageNum: Int = 1,
        pageSize: Int = 20,
        sortField: String = "createdAt",
        sortDir: String = "desc"
    ): PageResponse<LeadResponse> {
        val pageable = createPageable(pageNum, pageSize, sortField, sortDir)
        
        // 构建 Specification 查询条件
        val specification = Specification<Lead> { root, _, criteriaBuilder ->
            var predicate = criteriaBuilder.conjunction() // 相当于 AND true
            
            query.customerId?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("customerId"), it))
            }
            query.cid?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("cid"), it))
            }
            query.name?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get<String>("name"), "%$it%"))
            }
            query.status?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("status"), it))
            }
            query.owner?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("owner"), it))
            }
            query.source?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("source"), it))
            }
            query.intentProduct?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("intentProduct"), it))
            }
            query.intentLevel?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get<String>("intentLevel"), it))
            }
            query.startDate?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get<java.time.LocalDateTime>("createdAt"), it.atStartOfDay()))
            }
            query.endDate?.let {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get<java.time.LocalDateTime>("createdAt"), it.atTime(23, 59, 59)))
            }
            
            predicate
        }
        
        val page = leadRepository.findAll(specification, pageable)
        
        return PageResponse(
            total = page.totalElements,
            pageNum = page.number + 1,
            pageSize = page.size,
            totalPages = page.totalPages,
            records = page.content.map { LeadResponse.fromEntity(it) }
        )
    }
    
    /**
     * 添加需求
     */
    fun addRequirement(id: Long, requirement: Lead.Requirement): LeadResponse {
        val lead = leadRepository.findById(id)
            .orElseThrow { IllegalArgumentException("潜在客户不存在: $id") }
        
        lead.addRequirement(requirement)
        lead.updatedAt = java.time.LocalDateTime.now()
        
        val updatedLead = leadRepository.save(lead)
        return LeadResponse.fromEntity(updatedLead)
    }
    
    /**
     * 添加跟进日志
     */
    fun addFollowLog(id: Long, followLog: Lead.FollowLog): LeadResponse {
        val lead = leadRepository.findById(id)
            .orElseThrow { IllegalArgumentException("潜在客户不存在: $id") }
        
        lead.addFollowLog(followLog)
        lead.updatedAt = java.time.LocalDateTime.now()
        
        val updatedLead = leadRepository.save(lead)
        return LeadResponse.fromEntity(updatedLead)
    }
    
    /**
     * 创建分页参数
     */
    private fun createPageable(
        pageNum: Int,
        pageSize: Int,
        sortField: String,
        sortDir: String
    ): Pageable {
        val sort = if (sortDir.equals("desc", ignoreCase = true)) {
            Sort.by(sortField).descending()
        } else {
            Sort.by(sortField).ascending()
        }
        
        return PageRequest.of(pageNum - 1, pageSize, sort)
    }
}