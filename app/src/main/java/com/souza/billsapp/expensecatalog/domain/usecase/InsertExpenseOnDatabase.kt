package com.souza.billsapp.expensecatalog.domain.usecase

import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository

class InsertExpenseOnDatabase(private val expenseRepository: ExpenseCatalogRepository) {
    suspend operator fun invoke(data: Expense) = expenseRepository.insertData(data)
}
