package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.PaymentMethodEntity
import com.expenses.tracker.backend.models.response.PaymentMethod

object PaymentMethodMapper {
    fun toResponse(entity: PaymentMethodEntity?): PaymentMethod {
        requireNotNull(entity) { "Payment Method Entity cannot be null" }

        return PaymentMethod(
            id = entity.id,
            name = entity.name,
            methodType = entity.methodType,
            closingDay = entity.closingDay
        )
    }

    fun toResponse(entities: List<PaymentMethodEntity>): List<PaymentMethod> = entities.map { toResponse(it) }
}