package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.PaymentMethodEntity
import com.expenses.tracker.backend.models.response.PaymentMethod

fun PaymentMethodEntity.toResponse(): PaymentMethod = PaymentMethod(
    id = id,
    name = name,
    methodType = methodType,
    closingDay = closingDay,
)

fun List<PaymentMethodEntity>.toResponse(): List<PaymentMethod> = map { it.toResponse() }
