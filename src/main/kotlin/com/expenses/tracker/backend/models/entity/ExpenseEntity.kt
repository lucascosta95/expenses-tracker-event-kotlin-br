package com.expenses.tracker.backend.models.entity

import jakarta.persistence.*

@Entity
@Table(name = "expense")
data class ExpenseEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @Column(name = "method_id")
    var methodId: Long,

    var reserved: Double,
    var spent: Double
)