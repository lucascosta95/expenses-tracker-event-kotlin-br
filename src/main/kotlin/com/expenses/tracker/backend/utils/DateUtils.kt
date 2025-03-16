package com.expenses.tracker.backend.utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

object DateUtils {
    fun truncateToStartOfMonth(date: ZonedDateTime?): LocalDateTime {
        return truncateToStartOfMonth(date?.toLocalDateTime())
    }

    fun truncateToStartOfMonth(date: LocalDateTime?): LocalDateTime {
        requireNotNull(date) { "The date cannot be null." }

        return date.with(TemporalAdjusters.firstDayOfMonth())
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
    }
}