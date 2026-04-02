package com.example.wealth.controller

import com.example.wealth.dto.*
import com.example.wealth.entity.Lead
import com.example.wealth.service.LeadService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * 潜在客户控制器
 */
@RestController
@RequestMapping("/leads")
class LeadController(private val leadService: LeadService) {
    
    /**
     * 创建潜在客户
     */
    @PostMapping
    fun createLead(@Valid @RequestBody request: LeadCreateRequest): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.createLead(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseDTO.success(lead))
    }
    
    /**
     * 根据ID查询潜在客户
     */
    @GetMapping("/{id}")
    fun getLeadById(@PathVariable id: Long): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.getLeadById(id)
        return ResponseEntity.ok(ResponseDTO.success(lead))
    }
    
    /**
     * 根据客户编号查询潜在客户
     */
    @GetMapping("/cid/{cid}")
    fun getLeadByCid(@PathVariable cid: String): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.getLeadByCid(cid)
        return ResponseEntity.ok(ResponseDTO.success(lead))
    }
    
    /**
     * 更新潜在客户
     */
    @PutMapping("/{id}")
    fun updateLead(
        @PathVariable id: Long,
        @Valid @RequestBody request: LeadUpdateRequest
    ): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.updateLead(id, request)
        return ResponseEntity.ok(ResponseDTO.success(lead))
    }
    
    /**
     * 删除潜在客户
     */
    @DeleteMapping("/{id}")
    fun deleteLead(@PathVariable id: Long): ResponseEntity<ResponseDTO<Void>> {
        leadService.deleteLead(id)
        return ResponseEntity.ok(ResponseDTO.success())
    }
    
    /**
     * 分页查询潜在客户列表
     */
    @PostMapping("/list")
    fun getLeads(
        @RequestBody(required = false) request: LeadQueryRequest?
    ): ResponseEntity<ResponseDTO<PageResponse<LeadResponse>>> {
        val queryRequest = request ?: LeadQueryRequest()
        val page = leadService.getLeads(
            queryRequest,
            queryRequest.pageNum,
            queryRequest.pageSize,
            queryRequest.sortField,
            queryRequest.sortDir
        )
        return ResponseEntity.ok(ResponseDTO.success(page))
    }
    
    /**
     * 添加需求
     */
    @PostMapping("/{id}/requirements")
    fun addRequirement(
        @PathVariable id: Long,
        @RequestBody requirement: Lead.Requirement
    ): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.addRequirement(id, requirement)
        return ResponseEntity.ok(ResponseDTO.success(lead))
    }
    
    /**
     * 添加跟进日志
     */
    @PostMapping("/{id}/follow-logs")
    fun addFollowLog(
        @PathVariable id: Long,
        @RequestBody followLog: Lead.FollowLog
    ): ResponseEntity<ResponseDTO<LeadResponse>> {
        val lead = leadService.addFollowLog(id, followLog)
        return ResponseEntity.ok(ResponseDTO.success(lead))
    }
    
    /**
     * 异常处理
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ResponseDTO<Void>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.error(ex.message ?: "请求参数错误"))
    }
    
    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ResponseDTO<Void>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDTO.error("服务器内部错误"))
    }
}