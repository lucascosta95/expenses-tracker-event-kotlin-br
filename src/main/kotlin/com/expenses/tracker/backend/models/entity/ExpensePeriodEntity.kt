package com.expenses.tracker.backend.models.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "expense_period")
class ExpensePeriodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "expense_id")
    val expenseId: Long?,

    var name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "method_id", referencedColumnName = "id", insertable = false, updatable = false)
    val method: PaymentMethodEntity? = null,

    @Column(name = "method_id")
    var methodId: Long,

    @Column(name = "reference_month")
    var referenceMonth: LocalDateTime,

    var reserved: Double,
    var spent: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpensePeriodEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}