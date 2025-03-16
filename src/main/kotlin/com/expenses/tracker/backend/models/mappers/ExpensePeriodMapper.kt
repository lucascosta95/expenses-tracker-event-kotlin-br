package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.entity.ExpensePeriodEntity
import com.expenses.tracker.backend.models.request.CreateExpensePeriod
import com.expenses.tracker.backend.models.response.ExpensePeriod
import com.expenses.tracker.backend.utils.DateUtils

object ExpensePeriodMapper {

    fun toResponse(entity: ExpensePeriodEntity): ExpensePeriod = ExpensePeriod(
        id = entity.id,
        name = entity.name,
        reserved = entity.reserved,
        spent = entity.spent,
        method = entity.method?.let { PaymentMethodMapper.toResponse(it) },
        methodId = entity.methodId,
        referenceMonth = entity.referenceMonth
    )

    fun toResponse(entities: List<ExpensePeriodEntity>): List<ExpensePeriod> = entities.map { toResponse(it) }

    fun createExpensePeriodToExpensePeriodEntity(
        createExpensePeriod: CreateExpensePeriod,
        expense: ExpenseEntity
    ): ExpensePeriodEntity {
        return ExpensePeriodEntity(
            expenseId = expense.id,
            name = createExpensePeriod.name,
            reserved = createExpensePeriod.reserved,
            spent = createExpensePeriod.spent,
            methodId = createExpensePeriod.methodId,
            referenceMonth = DateUtils.truncateToStartOfMonth(createExpensePeriod.referenceMonth)
        )
    }

}