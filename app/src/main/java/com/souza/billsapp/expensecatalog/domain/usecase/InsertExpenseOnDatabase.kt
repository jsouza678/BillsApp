package com.souza.billsapp.expensecatalog.domain.usecase

import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository

class InsertExpenseOnDatabase(private val expenseRepository: ExpenseCatalogRepository) {
    operator fun invoke(data: Expense) = expenseRepository.insertData(data)
}
