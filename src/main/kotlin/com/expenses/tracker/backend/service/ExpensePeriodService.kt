package com.expenses.tracker.backend.service

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.entity.ExpensePeriodEntity
import com.expenses.tracker.backend.models.exceptions.NotFoundException
import com.expenses.tracker.backend.models.mappers.toEntity
import com.expenses.tracker.backend.models.mappers.toResponse
import com.expenses.tracker.backend.models.request.CopyPreviousMonth
import com.expenses.tracker.backend.models.request.CreateExpensePeriod
import com.expenses.tracker.backend.models.request.UpdateExpensePeriod
import com.expenses.tracker.backend.models.response.ExpensePeriod
import com.expenses.tracker.backend.repository.ExpensePeriodRepository
import com.expenses.tracker.backend.repository.ExpenseRepository
import com.expenses.tracker.backend.repository.PaymentMethodRepository
import com.expenses.tracker.backend.utils.start
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class ExpensePeriodService(
    private val expenseRepository: ExpenseRepository,
    private val expensePeriodRepository: ExpensePeriodRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) {

    fun getAllByMonth(month: ZonedDateTime): List<ExpensePeriod> =
        expensePeriodRepository
            .findAllByReferenceMonth(month.start)
            .toResponse()

    fun getById(id: Long): ExpensePeriod = expensePeriodRepository
        .findById(id)
        .getOrNull()
        ?.toResponse()
        ?: throw NotFoundException("Expense period with ID $id not found")

    fun updateById(id: Long, updateExpensePeriod: UpdateExpensePeriod): ExpensePeriod {
        var expensePeriod = expensePeriodRepository
            .findById(id)
            .getOrNull()
            ?: throw NotFoundException("Expense period with ID $id not found")

        if (updateExpensePeriod.updateRecurrence) {
            val expenseId = expensePeriod.expenseId ?: throw IllegalStateException("Expense ID is null")

            expenseRepository
                .findById(expenseId)
                .getOrNull()
                ?.let { updateExpenseEntity(it, updateExpensePeriod) }
                ?.let(expenseRepository::save)

            expensePeriodRepository
                .findAllByExpenseId(expenseId)
                .filter { it.referenceMonth > updateExpensePeriod.referenceMonth }
                .map { updateExpensePeriodEntity(it, updateExpensePeriod) }
                .takeIf { it.isNotEmpty() }
                ?.let { expensePeriodRepository.saveAll(it) }

        }

        expensePeriod = updateExpensePeriodEntity(expensePeriod, updateExpensePeriod)
        expensePeriod.spent = updateExpensePeriod.spent
        expensePeriod.referenceMonth = updateExpensePeriod.referenceMonth.start

        return expensePeriodRepository
            .save(expensePeriod)
            .toResponse()
    }

    fun create(createExpensePeriod: CreateExpensePeriod): ExpensePeriod {
        paymentMethodRepository
            .findById(createExpensePeriod.methodId)
            .getOrElse { throw NotFoundException("Payment method with ID ${createExpensePeriod.methodId} not found") }

        return createExpensePeriod
            .toEntity()
            .let(expenseRepository::save)
            .let { createExpensePeriod.toEntity(it) }
            .let(expensePeriodRepository::save)
            .toResponse()
    }

    fun copyPreviousMonth(copyPreviousMonth: CopyPreviousMonth): List<ExpensePeriod> {
        val currentMonth = copyPreviousMonth.currentMonth.start
        val currentExpensesMap = expensePeriodRepository
            .findAllByReferenceMonth(currentMonth)
            .associateBy { it.expenseId }
            .toMutableMap()

        val previousMonth = currentMonth.minusMonths(1)
        val expensesToSave = expensePeriodRepository
            .findAllByReferenceMonth(previousMonth)
            .map {
                currentExpensesMap.getOrPut(it.expenseId) {
                    ExpensePeriodEntity(
                        name = it.name,
                        expenseId = it.expenseId,
                        methodId = it.methodId,
                        reserved = it.reserved,
                        spent = 0.0,
                        referenceMonth = currentMonth
                    )
                }.apply {
                    name = it.name
                    methodId = it.methodId
                    reserved = it.reserved
                }
            }

        if (expensesToSave.isEmpty()) {
            return emptyList()
        }

        return expensePeriodRepository
            .saveAll(expensesToSave)
            .toResponse()
    }

    fun deleteById(id: Long) {
        val expensePeriod = expensePeriodRepository
            .findById(id)
            .getOrElse { throw NotFoundException("Expense period with ID $id not found") }

        expensePeriod.expenseId ?: throw IllegalStateException("Expense ID is null")

        val eps = expensePeriodRepository.findAllByExpenseId(expensePeriod.expenseId)

        expensePeriodRepository.deleteById(id)
        if (eps.size == 1) {
            expenseRepository.deleteById(expensePeriod.expenseId)
        }
    }

    private fun updateExpenseEntity(
        expense: ExpenseEntity,
        updateExpensePeriod: UpdateExpensePeriod
    ): ExpenseEntity = expense.apply {
        name = updateExpensePeriod.name
        methodId = updateExpensePeriod.methodId
        reserved = updateExpensePeriod.reserved
    }

    private fun updateExpensePeriodEntity(
        expensePeriod: ExpensePeriodEntity,
        updateExpensePeriod: UpdateExpensePeriod
    ): ExpensePeriodEntity = expensePeriod.apply {
        name = updateExpensePeriod.name
        methodId = updateExpensePeriod.methodId
        reserved = updateExpensePeriod.reserved
    }
}