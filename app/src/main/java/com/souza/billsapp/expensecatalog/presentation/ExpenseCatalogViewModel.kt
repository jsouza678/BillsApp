package com.souza.billsapp.expensecatalog.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.expensecatalog.data.ExpenseCatalogRepositoryImpl
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.GetExpensesWithPaidStatusFromDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.InsertExpenseOnDatabase
import com.souza.billsapp.expensecatalog.domain.usecase.UpdateExpenseOnDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseCatalogViewModel(
    private val getExpensesFromDatabase: GetExpensesFromDatabase,
    private val getExpensesWithPaidStatusFromDatabase: GetExpensesWithPaidStatusFromDatabase,
    private val insertExpenseOnDatabase: InsertExpenseOnDatabase,
    private val updateExpenseOnDatabase: UpdateExpenseOnDatabase
) : ViewModel() {

    private val expenseCatalogRepositoryImpl = ExpenseCatalogRepositoryImpl()
    private val _dataList = MutableLiveData<FirestoreRecyclerOptions<Expense>>()
    private val coroutineScope = Dispatchers.IO
    val dataList: LiveData<FirestoreRecyclerOptions<Expense>>
        get() = _dataList

    init {
        noFilterExpense()
    }

    fun insertExpense(data: Expense) {
        viewModelScope.launch(context = coroutineScope) {
            insertExpenseOnDatabase(data)
        }
    }

    fun updateExpense(data: Expense, document: String) {
        viewModelScope.launch(context = coroutineScope) {
            updateExpenseOnDatabase(data, document)
        }
    }

    fun filteredListOnMLiveData() {
        viewModelScope.launch(context = coroutineScope) {
            _dataList.postValue(getExpensesWithPaidStatusFromDatabase.invoke())
        }
    }

    fun unfilteredListOnMLiveData() = viewModelScope.launch(context = coroutineScope) {
        _dataList.postValue(getExpensesFromDatabase.invoke())
    }

    private fun noFilterExpense() {
        viewModelScope.launch(context = coroutineScope) {
            _dataList.postValue(getExpensesFromDatabase.invoke())
        }
    }

    fun insertExpenseImageAttach(imageUri: Uri) = expenseCatalogRepositoryImpl.insertExpenseImageAttachOnStorage(imageUri)

    fun updateExpenseImageURLOnLiveData(): LiveData<String?> = expenseCatalogRepositoryImpl.attachURLResult()
}
