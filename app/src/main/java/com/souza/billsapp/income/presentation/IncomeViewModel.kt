package com.souza.billsapp.income.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.data.Income
import com.souza.billsapp.data.IncomeRepository

public class IncomeViewModel : ViewModel() {

    private var incomeRepository : IncomeRepository = IncomeRepository()
    private val _dataList = MutableLiveData<FirestoreRecyclerOptions<Income>>()
    val dataList : LiveData<FirestoreRecyclerOptions<Income>>
    get() = _dataList

    init{
        _dataList.postValue(noFilterIncome())
    }

    fun insertIncome(data: Income) {
        incomeRepository.insertData(data)
    }

    fun updateIncome(data: Income, document: String) {
        incomeRepository.updateData(data, document)
    }

    fun filteredListOnMLiveData() = _dataList.postValue(filterByMonth())

    fun unfilteredListOnMLiveData() = _dataList.postValue(noFilterIncome())

    private fun filterByMonth() : FirestoreRecyclerOptions<Income> = incomeRepository.getMonthlyData()

    private fun noFilterIncome() : FirestoreRecyclerOptions<Income> = incomeRepository.getData()

    fun insertIncomeImageAttach (imageUri : Uri) = incomeRepository.insertIncomeImageAttachOnStorage(imageUri)

    fun updateIncomeImageURLOnLiveData() : LiveData<String?> = incomeRepository.attachURLResult
}