package com.souza.billsapp.expensecatalog.domain.usecase

import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository

class GetExpensesWithPaidStatusFromDatabase(private val expenseRepository: ExpenseCatalogRepository) {
    suspend operator fun invoke() = expenseRepository.getMonthlyData()
}
