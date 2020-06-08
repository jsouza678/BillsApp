package com.souza.billsapp.expensecatalog.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.expensecatalog.domain.repository.ExpenseCatalogRepository
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.EMPTY_STRING
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.USERS_TABLE_NAME
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.WAS_PAID_FILTER_STATUS
import java.util.Calendar

class ExpenseCatalogRepositoryImpl : ExpenseCatalogRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes: Int = calendar.get(Calendar.MONTH)
    private val ano: Int = calendar.get(Calendar.YEAR)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private val collectionName: String = "despesas_$mes" + "_" + "$ano"
    private val queryTest = db.document("users/$userId").collection(collectionName)
    private val queryFilteredByReceivedStatus =
        db.document("users/$userId").collection(collectionName).whereEqualTo(WAS_PAID_FILTER_STATUS, true)
    private var imageUrl = EMPTY_STRING
    private val databaseReference: FirebaseStorage = FirebaseStorage.getInstance()
    private val path: String = "users/$auth/$collectionName/${java.util.UUID.randomUUID()}.png"
    private val reference = databaseReference.getReference(path)
    private val _attachURLResult = MutableLiveData<String?>()

    override fun attachURLResult(): LiveData<String?> = _attachURLResult

    override fun getData(): FirestoreRecyclerOptions<Expense> {
        return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(queryTest, Expense::class.java)
            .build()
    }

    override fun getMonthlyData(): FirestoreRecyclerOptions<Expense> {
        return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(queryFilteredByReceivedStatus, Expense::class.java)
            .build()
    }

    override fun insertData(data: Expense) {
        db.collection(USERS_TABLE_NAME).document(userId).collection(collectionName)
            .document()
            .set(data)
            .addOnSuccessListener {
                // Success. Data inserted.
            }
            .addOnFailureListener {
                // Failed. Data not inserted.
            }
    }

    override fun updateData(data: Expense, document: String) {
        db.collection(USERS_TABLE_NAME).document(userId).collection(collectionName)
            .document(document)
            .set(data)
            .addOnSuccessListener {
                // Success. Data updated.
            }
            .addOnFailureListener {
                // Failed. Data not updated.
            }
    }

    override fun insertExpenseImageAttachOnStorage(imageUri: Uri) {
        reference
            .putFile(imageUri)
            .addOnSuccessListener {
                reference.downloadUrl
                    .addOnSuccessListener {
                        imageUrl = it.toString()
                        _attachURLResult.postValue(it.toString())
                    }
            }.addOnFailureListener { }
    }
}
