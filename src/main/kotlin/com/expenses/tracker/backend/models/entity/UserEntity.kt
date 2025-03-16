package com.expenses.tracker.backend.models.entity

import jakarta.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,
    val email: String,

    @Column(name = "hash_password")
    val hashPassword: String
)