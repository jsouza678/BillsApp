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
        _dataList.postValue(noFilterExpense())
    }

    fun insertExpense(data: Expense) {
        expenseRepository.insertData(data)
    }

    fun updateExpense(data: Expense, document: String) {
        expenseRepository.updateData(data, document)
    }

    fun filteredListOnMLiveData() = _dataList.postValue(filterByMonth())

    fun unfilteredListOnMLiveData() = _dataList.postValue(noFilterExpense())

    fun filterByMonth() : FirestoreRecyclerOptions<Expense> {
        return expenseRepository.getMonthlyData()
    }

    fun noFilterExpense() : FirestoreRecyclerOptions<Expense> {
        return expenseRepository.getData()
    }
}