package com.expenses.tracker.backend.repository

import com.expenses.tracker.backend.models.entity.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository : JpaRepository<ExpenseEntity, Long>