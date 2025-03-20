package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.request.CreateExpensePeriod

private const val DEFAULT_SPENT_VALUE = 0.0

fun CreateExpensePeriod.toEntity(): ExpenseEntity = ExpenseEntity(
    name = name,
    reserved = reserved,
    spent = DEFAULT_SPENT_VALUE,
    methodId = methodId,
)
