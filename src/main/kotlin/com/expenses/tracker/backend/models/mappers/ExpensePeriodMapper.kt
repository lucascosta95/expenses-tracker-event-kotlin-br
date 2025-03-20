package com.expenses.tracker.backend.models.mappers

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.entity.ExpensePeriodEntity
import com.expenses.tracker.backend.models.request.CreateExpensePeriod
import com.expenses.tracker.backend.models.response.ExpensePeriod
import com.expenses.tracker.backend.utils.start

fun ExpensePeriodEntity.toResponse(): ExpensePeriod = ExpensePeriod(
    id = id,
    name = name,
    reserved = reserved,
    spent = spent,
    method = method?.toResponse(),
    methodId = methodId,
    referenceMonth = referenceMonth,
)

fun List<ExpensePeriodEntity>.toResponse(): List<ExpensePeriod> = map { it.toResponse() }

fun CreateExpensePeriod.toEntity(expense: ExpenseEntity): ExpensePeriodEntity = ExpensePeriodEntity(
    expenseId = expense.id,
    name = name,
    reserved = reserved,
    spent = spent,
    methodId = methodId,
    referenceMonth = referenceMonth.start,
)
