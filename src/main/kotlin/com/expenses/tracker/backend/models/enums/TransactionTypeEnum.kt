package com.expenses.tracker.backend.models.enums

enum class TransactionTypeEnum(val code: String) {
    EARNINGS("R"),
    EXPENSE("D"),
    INVESTMENT("I"),
}