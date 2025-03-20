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
            val expenseId = expensePeriod.expenseId
                ?: throw IllegalStateException("Expense ID is null")

            expenseRepository
                .findById(expenseId)
                .getOrNull()
                ?.update(updateExpensePeriod)
                ?.let(expenseRepository::save)

            expensePeriodRepository
                .findAllByExpenseId(expenseId)
                .filter { it.referenceMonth > updateExpensePeriod.referenceMonth }
                .map { it.update(updateExpensePeriod) }
                .takeIf { it.isNotEmpty() }
                ?.let(expensePeriodRepository::saveAll)
        }

        expensePeriod = expensePeriod.update(updateExpensePeriod)

        return expensePeriodRepository
            .save(expensePeriod)
            .toResponse()
    }

    fun create(createExpensePeriod: CreateExpensePeriod): ExpensePeriod {
        paymentMethodRepository
            .findById(createExpensePeriod.methodId)
            .getOrNull()
            ?: throw NotFoundException("Payment method with ID ${createExpensePeriod.methodId} not found")

        return createExpensePeriod
            .toEntity()
            .let(expenseRepository::save)
            .let { createExpensePeriod.toEntity(it) }
            .let(expensePeriodRepository::save)
            .toResponse()
    }

    fun copyPreviousMonth(copyPreviousMonth: CopyPreviousMonth): List<ExpensePeriod> {
        val currentMonth = copyPreviousMonth.currentMonth.start
        val previousMonth = currentMonth.minusMonths(1)

        val currentExpenses = expensePeriodRepository
            .findAllByReferenceMonth(currentMonth)
            .associateByTo(mutableMapOf()) { it.expenseId }

        val expensesToSave = expensePeriodRepository
            .findAllByReferenceMonth(previousMonth)
            .map { previousPeriod ->
                currentExpenses.getOrPut(previousPeriod.expenseId) {
                    ExpensePeriodEntity(
                        name = previousPeriod.name,
                        expenseId = previousPeriod.expenseId,
                        methodId = previousPeriod.methodId,
                        reserved = previousPeriod.reserved,
                        spent = 0.0,
                        referenceMonth = currentMonth
                    )
                }.apply {
                    name = previousPeriod.name
                    methodId = previousPeriod.methodId
                    reserved = previousPeriod.reserved
                }
            }

        return expensePeriodRepository.saveAll(expensesToSave).toResponse()
            .takeIf { it.isNotEmpty() }
            ?: emptyList()
    }

    fun deleteById(id: Long) {
        val expensePeriod = expensePeriodRepository
            .findById(id)
            .getOrNull()
            ?: throw NotFoundException("Expense period with ID $id not found")

        val expenseId = expensePeriod.expenseId ?: throw IllegalStateException("Expense ID is null")

        val eps = expensePeriodRepository.findAllByExpenseId(expenseId)

        expensePeriodRepository.deleteById(id)
        if (eps.size == 1) {
            expenseRepository.deleteById(expenseId)
        }
    }
}

private fun ExpenseEntity.update(updateExpensePeriod: UpdateExpensePeriod): ExpenseEntity = apply {
    name = updateExpensePeriod.name
    methodId = updateExpensePeriod.methodId
    reserved = updateExpensePeriod.reserved
}

private fun ExpensePeriodEntity.update(updateExpensePeriod: UpdateExpensePeriod): ExpensePeriodEntity = apply {
    name = updateExpensePeriod.name
    methodId = updateExpensePeriod.methodId
    reserved = updateExpensePeriod.reserved
    spent = updateExpensePeriod.spent
    referenceMonth = updateExpensePeriod.referenceMonth.start
}