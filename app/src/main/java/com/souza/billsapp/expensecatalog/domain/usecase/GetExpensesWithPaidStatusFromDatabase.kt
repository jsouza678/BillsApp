package com.souza.billsapp.expensecatalog.domain.usecase

import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository

class GetExpensesWithPaidStatusFromDatabase(private val expenseRepository: ExpenseCatalogRepository) {
    operator fun invoke() = expenseRepository.getMonthlyData()
}
