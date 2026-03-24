package com.example.wealth.service.impl

import com.example.wealth.entity.User
import com.example.wealth.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found")
        
        val authorities = user.roles.flatMap { role ->
            role.permissions.map { permission ->
                SimpleGrantedAuthority(permission.code)
            }
        } + user.roles.map { role ->
            SimpleGrantedAuthority("ROLE_${role.name}")
        }
        
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            authorities
        )
    }
}