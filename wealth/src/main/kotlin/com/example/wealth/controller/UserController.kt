package com.example.wealth.controller

import com.example.wealth.entity.User
import com.example.wealth.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    
    @Autowired
    private lateinit var userService: UserService
    
    @Operation(summary = "获取所有用户", description = "返回系统中的所有用户列表")
    @ApiResponse(responseCode = "200", description = "成功获取用户列表")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.findAll())
    }
    
    @Operation(summary = "根据ID获取用户", description = "通过用户ID返回特定用户信息")
    @ApiResponse(responseCode = "200", description = "成功获取用户")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.findById(id)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "创建新用户", description = "创建一个新的用户账户")
    @ApiResponse(responseCode = "201", description = "用户创建成功")
    @ApiResponse(responseCode = "400", description = "请求参数无效")
    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.create(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }
    
    @Operation(summary = "更新用户信息", description = "更新指定ID的用户信息")
    @ApiResponse(responseCode = "200", description = "用户更新成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: User): ResponseEntity<User> {
        return try {
            ResponseEntity.ok(userService.update(id, user))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
    
    @Operation(summary = "删除用户", description = "删除指定ID的用户")
    @ApiResponse(responseCode = "204", description = "用户删除成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            userService.delete(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
}