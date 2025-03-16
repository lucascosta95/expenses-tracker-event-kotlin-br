package com.expenses.tracker.backend.controller

import com.expenses.tracker.backend.models.request.CopyPreviousMonth
import com.expenses.tracker.backend.models.request.CreateExpensePeriod
import com.expenses.tracker.backend.models.request.UpdateExpensePeriod
import com.expenses.tracker.backend.service.ExpensePeriodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

@Tag(name = "Expense Period", description = "Expense period management.")
@RestController
@RequestMapping("/api/expenses")
class ExpensePeriodController(private val service: ExpensePeriodService) {

    @Operation(summary = "List expenses by month")
    @ApiResponse(responseCode = "200", description = "Expenses list returned successfully.")
    @GetMapping
    fun getAllByMonth(@RequestParam month: ZonedDateTime) = ResponseEntity.ok(service.getAllByMonth(month))

    @Operation(summary = "Get expense by Id", description = "Returns a specific expense by Id.")
    @ApiResponse(responseCode = "200", description = "Expense found")
    @ApiResponse(responseCode = "404", description = "Expense not found")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = ResponseEntity.ok(service.getById(id))

    @Operation(summary = "Update an expense", description = "Updates an existing expense data.")
    @ApiResponse(responseCode = "200", description = "Expense updated successfully")
    @ApiResponse(responseCode = "404", description = "Expense not found")
    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Long, @RequestBody expense: UpdateExpensePeriod) =
        ResponseEntity.ok(service.updateById(id, expense))

    @Operation(summary = "Create a new expense", description = "Adds a new expense to the system.")
    @ApiResponse(responseCode = "200", description = "Expense created successfully")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @PostMapping
    fun create(@RequestBody expense: CreateExpensePeriod) = ResponseEntity.ok(service.create(expense))

    @Operation(
        summary = "Copy previous month expenses",
        description = "Duplicates expenses from previous month to current one."
    )
    @ApiResponse(responseCode = "200", description = "Expenses copied successfully")
    @PostMapping("/copy-previous-month")
    fun copyPreviousMonth(@RequestBody copyPreviousMonth: CopyPreviousMonth) =
        ResponseEntity.ok(service.copyPreviousMonth(copyPreviousMonth))

    @Operation(summary = "Delete an expense", description = "Removes an expense by ID.")
    @ApiResponse(responseCode = "200", description = "Expense deleted successfully")
    @ApiResponse(responseCode = "404", description = "Expense not found")
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = ResponseEntity.ok(service.deleteById(id))
}