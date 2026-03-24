package com.example.wealth.mapper

import com.example.wealth.dto.AccountDTO
import com.example.wealth.dto.AccountCreateRequest
import com.example.wealth.dto.AccountUpdateRequest
import com.example.wealth.dto.TransactionDTO
import com.example.wealth.dto.UserDTO
import com.example.wealth.dto.UserCreateRequest
import com.example.wealth.dto.UserUpdateRequest
import com.example.wealth.entity.Account
import com.example.wealth.entity.Transaction
import com.example.wealth.entity.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface EntityMapper {
    
    // 用户映射方法
    @Mapping(target = "password", ignore = true)
    fun userToUserDTO(user: User): UserDTO
    
    fun userCreateRequestToUser(userCreateRequest: UserCreateRequest): User
    
    @Mapping(target = "password", ignore = true)
    fun updateUserFromUserUpdateRequest(
        userUpdateRequest: UserUpdateRequest,
        @MappingTarget user: User
    )
    
    // 账户映射方法
    @Mapping(source = "user.id", target = "userId")
    fun accountToAccountDTO(account: Account): AccountDTO
    
    // 需要单独处理用户关联
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    fun accountCreateRequestToAccount(accountCreateRequest: AccountCreateRequest): Account
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    fun updateAccountFromAccountUpdateRequest(
        accountUpdateRequest: AccountUpdateRequest,
        @MappingTarget account: Account
    )
    
    // 交易映射方法
    @Mapping(source = "account.id", target = "accountId")
    fun transactionToTransactionDTO(transaction: Transaction): TransactionDTO
}