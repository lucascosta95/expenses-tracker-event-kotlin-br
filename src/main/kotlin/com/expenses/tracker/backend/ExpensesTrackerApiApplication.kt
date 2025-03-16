package com.expenses.tracker.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExpensesTrackerApiApplication

fun main(args: Array<String>) {
	runApplication<ExpensesTrackerApiApplication>(*args)
}
