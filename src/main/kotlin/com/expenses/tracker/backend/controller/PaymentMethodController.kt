package com.expenses.tracker.backend.controller

import com.expenses.tracker.backend.service.PaymentMethodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Payment Method", description = "Payment method management.")
@Controller
@RequestMapping("/api/payment-methods")
class PaymentMethodController(private val service: PaymentMethodService) {

    @Operation(summary = "List payment methods")
    @ApiResponse(responseCode = "200", description = "Payment methods list returned successfully.")
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())
}