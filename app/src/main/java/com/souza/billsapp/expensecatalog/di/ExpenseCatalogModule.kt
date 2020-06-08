package com.souza.billsapp.expensecatalog.di

import com.souza.billsapp.expensecatalog.data.ExpenseCatalogRepositoryImpl
import com.souza.billsapp.expensecatalog.presentation.ExpenseCatalogViewModel
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesWithPaidStatusFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.InsertExpenseOnDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.UpdateExpenseOnDatabase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments", "USELESS_CAST")
val expenseCatalogModule = module {

    // ViewModel
    viewModel {
        ExpenseCatalogViewModel(
            get<GetExpensesFromDatabase>(),
            get<GetExpensesWithPaidStatusFromDatabase>(),
            get<InsertExpenseOnDatabase>(),
            get<UpdateExpenseOnDatabase>()
        )
    }

    // UseCases
    factory {
        GetExpensesFromDatabase(
            get<ExpenseCatalogRepository>()
        )
    }

    factory {
        GetExpensesWithPaidStatusFromDatabase(
            get<ExpenseCatalogRepository>()
        )
    }

    factory {
        InsertExpenseOnDatabase(
            get<ExpenseCatalogRepository>()
        )
    }

    factory {
        UpdateExpenseOnDatabase(
            get<ExpenseCatalogRepository>()
        )
    }

    factory {
        ExpenseCatalogRepositoryImpl(
        ) as ExpenseCatalogRepository
    }
}
