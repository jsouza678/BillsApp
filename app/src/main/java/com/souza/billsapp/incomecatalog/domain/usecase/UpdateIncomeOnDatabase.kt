package com.souza.billsapp.incomecatalog.domain.usecase

import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class UpdateIncomeOnDatabase(private val incomeRepository: IncomeCatalogRepository) {
    suspend operator fun invoke(data: Income, document: String) = incomeRepository.updateData(data, document)
}
