package com.expenses.tracker.backend.models.response

import com.expenses.tracker.backend.models.enums.PaymentMethodTypeEnum

data class PaymentMethod(
    val id: Long? = 0,
    var name: String,
    var closingDay: Long?,
    var methodType: PaymentMethodTypeEnum?
)
