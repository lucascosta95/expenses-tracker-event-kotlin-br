package com.expenses.tracker.backend.models.entity

import jakarta.persistence.*

@Entity
@Table(name = "expense")
class ExpenseEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @Column(name = "method_id")
    var methodId: Long,

    var reserved: Double,
    var spent: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpenseEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}