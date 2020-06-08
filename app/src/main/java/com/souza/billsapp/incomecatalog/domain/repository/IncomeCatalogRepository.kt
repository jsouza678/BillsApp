package com.souza.billsapp.incomecatalog.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.incomecatalog.domain.Income

interface IncomeCatalogRepository {

    fun attachURLResult(): LiveData<String?>

    fun getData(): FirestoreRecyclerOptions<Income>

    fun getMonthlyData(): FirestoreRecyclerOptions<Income>

    fun insertData(data: Income)

    fun updateData(data: Income, document: String)

    fun insertIncomeImageAttachOnStorage(imageUri: Uri)
}
