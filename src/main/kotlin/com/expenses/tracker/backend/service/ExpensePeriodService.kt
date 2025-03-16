package com.expenses.tracker.backend.service

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import com.expenses.tracker.backend.models.entity.ExpensePeriodEntity
import com.expenses.tracker.backend.models.exceptions.NotFoundException
import com.expenses.tracker.backend.models.mappers.ExpenseMapper
import com.expenses.tracker.backend.models.mappers.ExpensePeriodMapper
import com.expenses.tracker.backend.models.request.CopyPreviousMonth
import com.expenses.tracker.backend.models.request.CreateExpensePeriod
import com.expenses.tracker.backend.models.request.UpdateExpensePeriod
import com.expenses.tracker.backend.models.response.ExpensePeriod
import com.expenses.tracker.backend.repository.ExpensePeriodRepository
import com.expenses.tracker.backend.repository.ExpenseRepository
import com.expenses.tracker.backend.repository.PaymentMethodRepository
import com.expenses.tracker.backend.utils.DateUtils
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ExpensePeriodService(
    private val expenseRepository: ExpenseRepository,
    private val expensePeriodRepository: ExpensePeriodRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) {

    fun getAllByMonth(month: ZonedDateTime): List<ExpensePeriod> =
        ExpensePeriodMapper.toResponse(
            expensePeriodRepository.findAllByReferenceMonth(
                DateUtils.truncateToStartOfMonth(
                    month
                )
            )
        )

    fun getById(id: Long): ExpensePeriod =
        ExpensePeriodMapper.toResponse(expensePeriodRepository.findById(id).orElseThrow {
            NotFoundException(
                "Expense period with ID $id not found"
            )
        })

    fun updateById(id: Long, updateExpensePeriod: UpdateExpensePeriod): ExpensePeriod {
        var expensePeriod = expensePeriodRepository.findById(id).orElseThrow {
            NotFoundException(
                "Expense period with ID $id not found"
            )
        }

        if (updateExpensePeriod.updateRecurrence) {
            expenseRepository.save(
                updateExpenseEntity(
                    expenseRepository.findById(expensePeriod.expenseId).get(),
                    updateExpensePeriod
                )
            )

            val eps = expensePeriodRepository.findAllByExpenseId(expensePeriod.expenseId)
                .filter { it.referenceMonth > updateExpensePeriod.referenceMonth }
                .map { updateExpensePeriodEntity(it, updateExpensePeriod) }

            if (eps.isNotEmpty())
                expensePeriodRepository.saveAll(eps)

        }

        expensePeriod = updateExpensePeriodEntity(expensePeriod, updateExpensePeriod)
        expensePeriod.spent = updateExpensePeriod.spent
        expensePeriod.referenceMonth = DateUtils.truncateToStartOfMonth(updateExpensePeriod.referenceMonth)
        return ExpensePeriodMapper.toResponse(expensePeriodRepository.save(expensePeriod))
    }

    fun create(createExpensePeriod: CreateExpensePeriod): ExpensePeriod {
        paymentMethodRepository.findById(createExpensePeriod.methodId).orElseThrow {
            NotFoundException(
                "Payment method with ID ${createExpensePeriod.methodId} not found"
            )
        }

        val expense = expenseRepository.save(ExpenseMapper.createExpensePeriodToExpense(createExpensePeriod))
        return ExpensePeriodMapper.toResponse(
            expensePeriodRepository.save(
                ExpensePeriodMapper.createExpensePeriodToExpensePeriodEntity(
                    createExpensePeriod, expense
                )
            )
        )
    }

    fun copyPreviousMonth(copyPreviousMonth: CopyPreviousMonth): List<ExpensePeriod> {
        val currentMonth = DateUtils.truncateToStartOfMonth(copyPreviousMonth.currentMonth)
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

        return ExpensePeriodMapper.toResponse(expensePeriodRepository.saveAll(expensesToSave))
    }

    fun deleteById(id: Long) {
        val expensePeriod = expensePeriodRepository.findById(id).orElseThrow {
            NotFoundException(
                "Expense period with ID $id not found"
            )
        }

        val eps = expensePeriodRepository.findAllByExpenseId(expensePeriod.expenseId)
        if (eps.size == 1) {
            expensePeriodRepository.deleteById(id)
            expenseRepository.deleteById(expensePeriod.expenseId)
        } else {
            expensePeriodRepository.deleteById(id)
        }
    }

    private fun updateExpenseEntity(
        expense: ExpenseEntity,
        updateExpensePeriod: UpdateExpensePeriod
    ): ExpenseEntity {
        expense.name = updateExpensePeriod.name
        expense.methodId = updateExpensePeriod.methodId
        expense.reserved = updateExpensePeriod.reserved
        return expense
    }

    private fun updateExpensePeriodEntity(
        expensePeriod: ExpensePeriodEntity,
        updateExpensePeriod: UpdateExpensePeriod
    ): ExpensePeriodEntity {
        expensePeriod.name = updateExpensePeriod.name
        expensePeriod.methodId = updateExpensePeriod.methodId
        expensePeriod.reserved = updateExpensePeriod.reserved
        return expensePeriod
    }
}