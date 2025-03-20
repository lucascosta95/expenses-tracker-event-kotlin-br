package com.expenses.tracker.backend.models.enums.converter

import com.expenses.tracker.backend.models.enums.PaymentMethodTypeEnum
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class PaymentMethodTypeEnumConverter : AttributeConverter<PaymentMethodTypeEnum, String> {

    override fun convertToDatabaseColumn(attribute: PaymentMethodTypeEnum?): String? =
        attribute?.code

    override fun convertToEntityAttribute(dbData: String?): PaymentMethodTypeEnum? =
        PaymentMethodTypeEnum
            .entries
            .find { it.code == dbData }
}