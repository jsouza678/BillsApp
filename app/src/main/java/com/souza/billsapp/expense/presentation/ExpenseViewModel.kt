package com.souza.billsapp.expense.presentation

import android.net.Uri
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

    private fun filterByMonth() : FirestoreRecyclerOptions<Expense> = expenseRepository.getMonthlyData()

    private fun noFilterExpense() : FirestoreRecyclerOptions<Expense> = expenseRepository.getData()

    fun insertExpenseImageAttach (imageUri : Uri) = expenseRepository.insertExpenseImageAttachOnStorage(imageUri)

    fun updateExpenseImageURLOnLiveData() : LiveData<String?> = expenseRepository.attachURLResult
}