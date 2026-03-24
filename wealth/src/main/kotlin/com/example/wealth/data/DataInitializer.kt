package com.example.wealth.data

import com.example.wealth.entity.Account
import com.example.wealth.entity.Permission
import com.example.wealth.entity.Role
import com.example.wealth.entity.User
import com.example.wealth.repository.AccountRepository
import com.example.wealth.repository.PermissionRepository
import com.example.wealth.repository.RoleRepository
import com.example.wealth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DataInitializer : CommandLineRunner {
    
    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Autowired
    private lateinit var accountRepository: AccountRepository
    
    @Autowired
    private lateinit var roleRepository: RoleRepository
    
    @Autowired
    private lateinit var permissionRepository: PermissionRepository
    
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    
    override fun run(vararg args: String?) {
        // 初始化权限
        if (permissionRepository.findAll().isEmpty()) {
            val permissions = listOf(
                Permission(code = "USER_READ", name = "查看用户", description = "查看用户信息的权限"),
                Permission(code = "USER_WRITE", name = "管理用户", description = "管理用户信息的权限"),
                Permission(code = "CUSTOMER_READ", name = "查看客户", description = "查看客户信息的权限"),
                Permission(code = "CUSTOMER_WRITE", name = "管理客户", description = "管理客户信息的权限"),
                Permission(code = "ACCOUNT_READ", name = "查看账户", description = "查看账户信息的权限"),
                Permission(code = "ACCOUNT_WRITE", name = "管理账户", description = "管理账户信息的权限")
            )
            val savedPermissions = permissionRepository.saveAll(permissions)
            
            // 初始化角色
            val adminRole = Role(name = "ADMIN", description = "系统管理员")
            adminRole.permissions = savedPermissions
            val userRole = Role(name = "USER", description = "普通用户")
            userRole.permissions = savedPermissions.subList(0, 3) // 只给普通用户分配查看权限
            
            val savedAdminRole = roleRepository.save(adminRole)
            val savedUserRole = roleRepository.save(userRole)
            
            // 创建默认管理员用户
            val adminUser = User(
                username = "admin",
                password = passwordEncoder.encode("admin123"),
                email = "admin@example.com",
                fullName = "系统管理员"
            )
            adminUser.roles = mutableListOf(savedAdminRole)
            val savedAdminUser = userRepository.save(adminUser)
            
            // 创建默认普通用户
            val normalUser = User(
                username = "user",
                password = passwordEncoder.encode("user123"),
                email = "user@example.com",
                fullName = "普通用户"
            )
            normalUser.roles = mutableListOf(savedUserRole)
            val savedNormalUser = userRepository.save(normalUser)
            
            // 为管理员创建示例账户
            val adminSavingsAccount = Account(
                accountNumber = "1234567890123456",
                accountType = "SAVINGS",
                balance = BigDecimal(10000.00),
                currencyCode = "USD",
                user = savedAdminUser
            )
            accountRepository.save(adminSavingsAccount)
            
            val adminCheckingAccount = Account(
                accountNumber = "6543210987654321",
                accountType = "CHECKING",
                balance = BigDecimal(5000.00),
                currencyCode = "USD",
                user = savedAdminUser
            )
            accountRepository.save(adminCheckingAccount)
            
            // 为普通用户创建示例账户
            val userSavingsAccount = Account(
                accountNumber = "1111222233334444",
                accountType = "SAVINGS",
                balance = BigDecimal(3000.00),
                currencyCode = "USD",
                user = savedNormalUser
            )
            accountRepository.save(userSavingsAccount)
            
            println("示例数据初始化完成")
        }
    }
}