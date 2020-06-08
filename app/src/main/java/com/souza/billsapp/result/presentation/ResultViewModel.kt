package com.souza.billsapp.result.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.souza.billsapp.result.data.ResultRepositoryImpl

class ResultViewModel : ViewModel() {

    private var resultRepository : ResultRepositoryImpl =
        ResultRepositoryImpl()

    init{
        resultRepository.getValueSum()
    }

    fun getValuesOnRefresh() {
        resultRepository.getValueSum()
    }

    fun updateSumResultOfIncomeOnLiveData() : LiveData<Float?> = resultRepository.incomeQueryResult
    fun updateSumResultOfExpenseOnLiveData() : LiveData<Float?> = resultRepository.expenseQueryResult
}