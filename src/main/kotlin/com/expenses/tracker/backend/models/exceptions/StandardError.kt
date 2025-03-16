package com.expenses.tracker.backend.models.exceptions

import java.time.LocalDateTime

data class StandardError(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)