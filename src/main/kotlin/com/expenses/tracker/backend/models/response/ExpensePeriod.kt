package com.expenses.tracker.backend.models.response

import java.time.LocalDateTime

data class ExpensePeriod(
    val id: Long,
    val name: String,
    val method: PaymentMethod?,
    val methodId: Long,
    val referenceMonth: LocalDateTime,
    val reserved: Double,
    val spent: Double
)
