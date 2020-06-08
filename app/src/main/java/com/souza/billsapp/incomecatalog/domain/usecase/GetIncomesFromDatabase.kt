package com.souza.billsapp.incomecatalog.domain.usecase

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class GetIncomesFromDatabase(private val incomeRepository: IncomeCatalogRepository) {
    operator fun invoke(): FirestoreRecyclerOptions<Income> = incomeRepository.getData()
}
