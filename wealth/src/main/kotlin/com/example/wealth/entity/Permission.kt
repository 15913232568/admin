package com.example.wealth.entity

import jakarta.persistence.*

@Entity
@Table(name = "permissions")
class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    
    @Column(nullable = false, unique = true)
    var code: String = "",
    
    @Column(nullable = false)
    var name: String = "",
    
    var description: String = "",
    
    @ManyToMany(mappedBy = "permissions")
    var roles: MutableList<Role> = mutableListOf()
)