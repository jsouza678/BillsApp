package com.souza.billsapp.result.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.souza.billsapp.data.IncomeRepository

public class ResultViewModel : ViewModel() {

    private var incomeRepository : IncomeRepository = IncomeRepository()
    private val _data= MutableLiveData<Int>()
    val data: LiveData<Int>
    get() = _data

    init{
        incomeRepository.getIncomesValueSum()
        //_data.postValue()
    }

    fun updateValueOnLiveData() : LiveData<Int?> = incomeRepository.incomeQueryResult
}