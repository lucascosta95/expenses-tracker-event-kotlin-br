package com.expenses.tracker.backend.models.entity

import com.expenses.tracker.backend.models.enums.TransactionTypeEnum
import com.expenses.tracker.backend.models.enums.converter.TransactionTypeEnumConverter
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "transaction")
class TransactionEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Convert(converter = TransactionTypeEnumConverter::class)
    val type: TransactionTypeEnum,

    @Column(name = "linked_expense_id")
    val linkedExpenseId: Long?,

    @Column(name = "method_id")
    val methodId: Long,

    val date: LocalDate,
    val amount: Double,

    @Column(name = "user_id")
    val userId: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransactionEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
