package com.souza.billsapp.incomecatalog.domain.usecase

import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class GetIncomesWithReceivedStatusFromDatabase(private val incomeRepository: IncomeCatalogRepository) {
    operator fun invoke() = incomeRepository.getMonthlyData()
}
