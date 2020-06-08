package com.souza.billsapp.expensecatalog.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.expensecatalog.domain.Expense

interface ExpenseCatalogRepository {

    fun attachURLResult(): LiveData<String?>

    fun getData(): FirestoreRecyclerOptions<Expense>

    fun getMonthlyData(): FirestoreRecyclerOptions<Expense>

    fun insertData(data: Expense)

    fun updateData(data: Expense, document: String)

    fun insertExpenseImageAttachOnStorage(imageUri: Uri)
}
