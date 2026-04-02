package com.example.wealth.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    
    @Column(nullable = false, unique = true)
    var name: String = "",
    
    var description: String = "",
    
    @ManyToMany(mappedBy = "roles")
    var users: MutableList<User> = mutableListOf(),
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableList<Permission> = mutableListOf()
)