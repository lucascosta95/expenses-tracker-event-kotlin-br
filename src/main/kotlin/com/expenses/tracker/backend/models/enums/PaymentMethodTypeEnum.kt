package com.expenses.tracker.backend.models.enums

enum class PaymentMethodTypeEnum(val code: String) {
    BRADESCO("B"),
    CASH("D"),
    DEBITCARD("A"),
    NUBANK("N"),
    PIX("P"),
    SANTANDER("S"),
}