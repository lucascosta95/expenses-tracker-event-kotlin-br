package com.expenses.tracker.backend.utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

val ZonedDateTime.start get(): LocalDateTime =
    toLocalDateTime().start

val LocalDateTime.start: LocalDateTime get() =
    with(TemporalAdjusters.firstDayOfMonth())
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
