package com.souza.billsapp.incomecatalog.domain.usecase

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class GetIncomesFromDatabase(private val incomeRepository: IncomeCatalogRepository) {
    suspend operator fun invoke(): FirestoreRecyclerOptions<Income> = incomeRepository.getData()
}
