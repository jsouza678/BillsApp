package com.souza.billsapp.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class IncomeRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes: Int = calendar.get(java.util.Calendar.MONTH)
    private val ano: Int = calendar.get(java.util.Calendar.YEAR)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private var incomesValueSum = 0
    private val collectionName: String = "receita_$mes" + "_" + "$ano"
    private val queryTest = db.document("users/$userId").collection(collectionName)
    private val queryFilteredByReceivedStatus =
        db.document("users/$userId").collection(collectionName).whereEqualTo("wasReceived", true)
    private val _incomeQueryResult = MutableLiveData<Int?>()
    val incomeQueryResult: LiveData<Int?> get() = _incomeQueryResult
    private var imageUrl = ""
    private val databaseReference: FirebaseStorage = FirebaseStorage.getInstance()
    private val path: String = "users/$auth/$collectionName/${java.util.UUID.randomUUID()}.png"
    private val reference = databaseReference.getReference(path)
    private val _attachURLResult = MutableLiveData<String?>()
    val attachURLResult: LiveData<String?> get() = _attachURLResult

    fun getData(): FirestoreRecyclerOptions<Income> {
        return FirestoreRecyclerOptions
            .Builder<Income>()
            .setQuery(queryTest, Income::class.java)
            .build()
    }

    fun getMonthlyData(): FirestoreRecyclerOptions<Income> {
        return FirestoreRecyclerOptions
            .Builder<Income>()
            .setQuery(queryFilteredByReceivedStatus, Income::class.java)
            .build()
    }

    fun getIncomeValueSum() {
        val t = db.collection("users").document(userId).collection(collectionName)
            .get().addOnSuccessListener {
                val number: MutableList<Income>? = it.toObjects(Income::class.java)
                if (number != null) {
                    for (i in 0 until number.size) {
                        incomesValueSum += number[i].value!!
                    }
                    _incomeQueryResult.postValue(incomesValueSum)
                }
            }
            .addOnFailureListener {
            }
    }

    fun insertData(data: Income) {
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

    fun updateData(data: Income, document: String) {
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

    fun insertIncomeImageAttachOnStorage(imageUri: Uri) {
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
