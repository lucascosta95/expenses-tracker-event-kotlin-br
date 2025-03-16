package com.expenses.tracker.backend.models.enums.converter

import com.expenses.tracker.backend.models.enums.TransactionTypeEnum
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class TransactionTypeEnumConverter : AttributeConverter<TransactionTypeEnum, String> {
    override fun convertToDatabaseColumn(attribute: TransactionTypeEnum?): String? {
        return attribute?.code
    }

    override fun convertToEntityAttribute(dbData: String?): TransactionTypeEnum? {
        return TransactionTypeEnum.entries.find { it.code == dbData }
    }
}