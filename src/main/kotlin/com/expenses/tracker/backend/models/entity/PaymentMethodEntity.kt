package com.expenses.tracker.backend.models.entity

import com.expenses.tracker.backend.models.enums.PaymentMethodTypeEnum
import com.expenses.tracker.backend.models.enums.converter.PaymentMethodTypeEnumConverter
import jakarta.persistence.*

@Entity
@Table(name = "payment_method")
class PaymentMethodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @Column(name = "closing_day")
    var closingDay: Long?,

    @Convert(converter = PaymentMethodTypeEnumConverter::class)
    var methodType: PaymentMethodTypeEnum?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentMethodEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}