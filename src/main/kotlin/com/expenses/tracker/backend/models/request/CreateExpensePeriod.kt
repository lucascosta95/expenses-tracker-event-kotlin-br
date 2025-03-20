package com.expenses.tracker.backend.models.request

import java.time.LocalDateTime

data class CreateExpensePeriod(
    val name: String,
    val reserved: Double,
    val spent: Double,
    val methodId: Long,
    val referenceMonth: LocalDateTime,
)
