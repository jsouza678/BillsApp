package com.souza.billsapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.data.Expense
import com.souza.billsapp.data.ExpenseRepository

public class ExpenseViewModel : ViewModel() {

    private var expenseRepository : ExpenseRepository = ExpenseRepository()
    private val _dataList = MutableLiveData<FirestoreRecyclerOptions<Expense>>()
    val dataList : LiveData<FirestoreRecyclerOptions<Expense>>
    get() = _dataList

    init{
        _dataList.postValue(expenseRepository.getData())
    }

    fun insertExpense(data: Expense) {
        expenseRepository.insertData(data)
    }

}