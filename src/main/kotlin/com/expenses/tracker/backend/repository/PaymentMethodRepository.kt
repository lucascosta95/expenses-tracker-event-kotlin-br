package com.expenses.tracker.backend.repository

import com.expenses.tracker.backend.models.entity.PaymentMethodEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentMethodRepository : JpaRepository<PaymentMethodEntity, Long>