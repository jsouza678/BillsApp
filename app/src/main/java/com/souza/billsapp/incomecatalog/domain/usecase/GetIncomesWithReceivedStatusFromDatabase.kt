package com.souza.billsapp.incomecatalog.domain.usecase

import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class GetIncomesWithReceivedStatusFromDatabase(private val incomeRepository: IncomeCatalogRepository) {
    suspend operator fun invoke() = incomeRepository.getMonthlyData()
}
