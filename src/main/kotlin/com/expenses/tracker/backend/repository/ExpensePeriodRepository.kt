package com.expenses.tracker.backend.repository

import com.expenses.tracker.backend.models.entity.ExpensePeriodEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ExpensePeriodRepository : JpaRepository<ExpensePeriodEntity, Long> {
    fun findAllByReferenceMonth(month: LocalDateTime): List<ExpensePeriodEntity>
    fun findAllByExpenseId(expenseId: Long): List<ExpensePeriodEntity>
}