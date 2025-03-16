package com.expenses.tracker.backend.models.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "expense_period")
data class ExpensePeriodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "expense_id")
    val expenseId: Long,

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
)