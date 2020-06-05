package com.souza.billsapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class IncomeRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes : Int = calendar.get(java.util.Calendar.MONTH)
    private val ano : Int = calendar.get(java.util.Calendar.YEAR)
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private var soma = 0
    private val collectionName: String = "receita_$mes"+"_"+"$ano"
    private val queryTest = db.document("users/$userId").collection(collectionName)
    private val queryFilteredByReceivedStatus = db.document("users/$userId").collection(collectionName).whereEqualTo("wasReceived", true)
    private val mdata = MutableLiveData<Int?>()
    val returnValue: LiveData<Int?>  get() = mdata

    fun getData() : FirestoreRecyclerOptions<Income> {
         return FirestoreRecyclerOptions
            .Builder<Income>()
            .setQuery(queryTest, Income::class.java)
            .build()
    }

    fun getMonthlyData() : FirestoreRecyclerOptions<Income> {
        return FirestoreRecyclerOptions
                .Builder<Income>()
                .setQuery(queryFilteredByReceivedStatus, Income::class.java)
                .build()
    }

    fun getDataS() {
        val t = db.collection("users").document(userId).collection(collectionName)
            .get().addOnSuccessListener {
                val number : MutableList<Income>? = it.toObjects(Income::class.java)
                if (number != null) {
                    for(i in 0 until number.size){
                        soma += number[i].value!!
                    }
                    mdata.postValue(soma)
                }
            }
            .addOnFailureListener {
            }
    }

    fun insertData(data : Income) {
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

    fun updateData(data : Income, document: String) {
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
}

