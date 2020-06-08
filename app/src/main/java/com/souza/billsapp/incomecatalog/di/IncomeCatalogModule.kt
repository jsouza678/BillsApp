package com.souza.billsapp.incomecatalog.di

import com.souza.billsapp.incomecatalog.data.IncomeCatalogRepositoryImpl
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesWithPaidStatusFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.InsertExpenseOnDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.UpdateExpenseOnDatabase
import com.souza.billsapp.incomecatalog.domain.repository.IncomeCatalogRepository
import com.souza.billsapp.incomecatalog.domain.usecase.GetIncomesFromDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.GetIncomesWithReceivedStatusFromDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.InsertIncomeOnDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.UpdateIncomeOnDatabase
import com.souza.billsapp.incomecatalog.presentation.IncomeCatalogViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments", "USELESS_CAST")
val incomeCatalogModule = module {

    // ViewModel
    viewModel {
        IncomeCatalogViewModel(
            get<GetIncomesFromDatabase>(),
            get<GetIncomesWithReceivedStatusFromDatabase>(),
            get<InsertIncomeOnDatabase>(),
            get<UpdateIncomeOnDatabase>()
        )
    }

    // UseCases
    factory {
        GetIncomesFromDatabase(
            get<IncomeCatalogRepository>()
        )
    }

    factory {
        GetIncomesWithReceivedStatusFromDatabase(
            get<IncomeCatalogRepository>()
        )
    }

    factory {
        InsertIncomeOnDatabase(
            get<IncomeCatalogRepository>()
        )
    }

    factory {
        UpdateIncomeOnDatabase(
            get<IncomeCatalogRepository>()
        )
    }


    factory {
        IncomeCatalogRepositoryImpl(
        ) as IncomeCatalogRepository
    }
}
