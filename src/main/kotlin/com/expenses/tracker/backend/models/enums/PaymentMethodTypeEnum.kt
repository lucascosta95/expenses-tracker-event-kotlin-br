package com.expenses.tracker.backend.models.enums

enum class PaymentMethodTypeEnum(val code: String) {
    PIX("P"),
    CASH("D"),
    DEBITCARD("A"),
    SANTANDER("S"),
    NUBANK("N"),
    BRADESCO("B");
}