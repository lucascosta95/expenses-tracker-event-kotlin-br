package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.request.CreateExpensePeriod

object ExpenseMapper {

    fun createExpensePeriodToExpense(expensePeriod: CreateExpensePeriod): ExpenseEntity {
        return ExpenseEntity(
            name = expensePeriod.name,
            reserved = expensePeriod.reserved,
            spent = 0.0,
            methodId = expensePeriod.methodId
        )
    }
}