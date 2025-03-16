package com.expenses.tracker.backend.service

import com.expenses.tracker.backend.models.mappers.PaymentMethodMapper
import com.expenses.tracker.backend.models.response.PaymentMethod
import com.expenses.tracker.backend.repository.PaymentMethodRepository
import org.springframework.stereotype.Service

@Service
class PaymentMethodService(private val repository: PaymentMethodRepository) {

    fun getAll(): List<PaymentMethod> = PaymentMethodMapper.toResponse(repository.findAll())
}