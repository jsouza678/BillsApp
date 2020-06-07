package com.souza.billsapp.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class ExpenseRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes: Int = calendar.get(java.util.Calendar.MONTH)
    private val ano: Int = calendar.get(java.util.Calendar.YEAR)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private val collectionName: String = "despesas_$mes" + "_" + "$ano"
    private val queryTest = db.document("users/$userId").collection(collectionName)
    private val queryFilteredByPaidStatus =
        db.document("users/$userId").collection(collectionName).whereEqualTo("wasPaid", true)
    private var imageUrl = ""
    private val databaseReference: FirebaseStorage = FirebaseStorage.getInstance()
    private val path: String = "users/$auth/$collectionName/${java.util.UUID.randomUUID()}.png"
    private val reference = databaseReference.getReference(path)
    private val _attachURLResult = MutableLiveData<String?>()
    val attachURLResult: LiveData<String?> get() = _attachURLResult

    fun getData(): FirestoreRecyclerOptions<Expense> {
        return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(queryTest, Expense::class.java)
            .build()
    }

    fun getMonthlyData(): FirestoreRecyclerOptions<Expense> {
        return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(queryFilteredByPaidStatus, Expense::class.java)
            .build()
    }

    fun insertData(data: Expense) {
        db.collection("users").document(userId).collection(collectionName)
            .document()
            .set(data)
            .addOnSuccessListener {
                //TODO
            }
            .addOnFailureListener {
                //TODO
            }
    }

    fun updateData(data: Expense, document: String) {
        db.collection("users").document(userId).collection(collectionName)
            .document(document)
            .set(data)
            .addOnSuccessListener {
                //TODO
            }
            .addOnFailureListener {
                //TODO
            }
    }

    fun insertExpenseImageAttachOnStorage(imageUri: Uri) {
        val uploadTask = reference
            .putFile(imageUri)
            .addOnSuccessListener {

                //Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show()
                reference.downloadUrl
                    .addOnSuccessListener {
                        imageUrl = it.toString()
                        _attachURLResult.postValue(it.toString())
                        //Toast.makeText(requireContext(), "$download_url", Toast.LENGTH_SHORT)
                        //.show()
                    }
            }.addOnFailureListener {

            }
    }
}
