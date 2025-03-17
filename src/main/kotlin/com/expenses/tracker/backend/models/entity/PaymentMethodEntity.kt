package com.expenses.tracker.backend.models.entity

import com.expenses.tracker.backend.models.enums.PaymentMethodTypeEnum
import com.expenses.tracker.backend.models.enums.converter.PaymentMethodTypeEnumConverter
import jakarta.persistence.*

@Entity
@Table(name = "payment_method")
data class PaymentMethodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @Column(name = "closing_day")
    var closingDay: Long?,

    @Convert(converter = PaymentMethodTypeEnumConverter::class)
    var methodType: PaymentMethodTypeEnum?
)