package com.souza.billsapp.incomecatalog.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.incomecatalog.data.IncomeCatalogRepositoryImpl
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.incomecatalog.domain.usecase.GetIncomesFromDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.GetIncomesWithReceivedStatusFromDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.InsertIncomeOnDatabase
import com.souza.billsapp.incomecatalog.domain.usecase.UpdateIncomeOnDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeCatalogViewModel(
    private val getIncomesFromDatabase: GetIncomesFromDatabase,
    private val getIncomesWithReceivedStatusFromDatabase: GetIncomesWithReceivedStatusFromDatabase,
    private val insertIncomeOnDatabase: InsertIncomeOnDatabase,
    private val updateIncomeOnDatabase: UpdateIncomeOnDatabase
) : ViewModel() {

    private val incomeCatalogRepositoryImpl = IncomeCatalogRepositoryImpl()
    private val _dataList = MutableLiveData<FirestoreRecyclerOptions<Income>>()
    private val coroutineScope = Dispatchers.IO
    val dataList: LiveData<FirestoreRecyclerOptions<Income>>
        get() = _dataList

    init {
        noFilterIncome()
    }

    fun insertIncome(data: Income) {
        viewModelScope.launch(context = coroutineScope) {
            insertIncomeOnDatabase(data)
        }
    }

    fun updateIncome(data: Income, document: String) {
        viewModelScope.launch(context = coroutineScope) {
            updateIncomeOnDatabase(data, document)
        }
    }

    fun filteredListOnMLiveData() {
        viewModelScope.launch(context = coroutineScope) {
            _dataList.postValue(getIncomesWithReceivedStatusFromDatabase.invoke())
        }
    }

    fun unfilteredListOnMLiveData() = viewModelScope.launch(context = coroutineScope) {
        _dataList.postValue(getIncomesFromDatabase.invoke())
    }

    private fun noFilterIncome() {
        viewModelScope.launch(context = coroutineScope) {
            _dataList.postValue(getIncomesFromDatabase.invoke())
        }
    }

    fun insertIncomeImageAttach(imageUri: Uri) = incomeCatalogRepositoryImpl.insertIncomeImageAttachOnStorage(imageUri)

    fun updateIncomeImageURLOnLiveData(): LiveData<String?> = incomeCatalogRepositoryImpl.attachURLResult()
}
