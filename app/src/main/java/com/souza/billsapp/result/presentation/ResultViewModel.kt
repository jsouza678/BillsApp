package com.souza.billsapp.result.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.souza.billsapp.data.ExpenseRepository
import com.souza.billsapp.data.IncomeRepository

public class ResultViewModel : ViewModel() {

    private var incomeRepository : IncomeRepository = IncomeRepository()
    private var expenseRepository : ExpenseRepository = ExpenseRepository()

    init{
        incomeRepository.getIncomeValueSum()
        expenseRepository.getExpenseValueSum()
    }

    fun updateSumResultOfIncomeOnLiveData() : LiveData<Int?> = incomeRepository.incomeQueryResult
    fun updateSumResultOfExpenseOnLiveData() : LiveData<Int?> = expenseRepository.expenseQueryResult
}