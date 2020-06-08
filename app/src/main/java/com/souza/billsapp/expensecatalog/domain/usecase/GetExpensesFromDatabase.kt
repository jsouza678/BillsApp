package com.souza.billsapp.expensecatalog.domain.usecase

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository

class GetExpensesFromDatabase(private val expenseRepository: ExpenseCatalogRepository) {
    suspend operator fun invoke(): FirestoreRecyclerOptions<Expense> = expenseRepository.getData()
}
