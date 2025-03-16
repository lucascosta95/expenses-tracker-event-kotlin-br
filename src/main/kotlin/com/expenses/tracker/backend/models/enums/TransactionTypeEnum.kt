package com.expenses.tracker.backend.models.enums

enum class TransactionTypeEnum(val code: String) {
    EXPENSE("D"),
    EARNINGS("R"),
    INVESTMENT("I");
}