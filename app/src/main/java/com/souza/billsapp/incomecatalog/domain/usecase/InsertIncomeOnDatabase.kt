package com.souza.billsapp.incomecatalog.domain.usecase

import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository

class InsertIncomeOnDatabase(private val incomeRepository: IncomeCatalogRepository) {
    suspend operator fun invoke(data: Income) = incomeRepository.insertData(data)
}
