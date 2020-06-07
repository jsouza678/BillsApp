package com.souza.billsapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ResultRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes: Int = calendar.get(Calendar.MONTH)
    private val ano: Int = calendar.get(Calendar.YEAR)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private var expenseValueSum: Float = 0F
    private var incomesValueSum: Float = 0F
    private val collectionNameExpenses: String = "despesas_$mes" + "_" + "$ano"
    private val collectionNameIncomes: String = "receita_$mes" + "_" + "$ano"
    private val _expenseQueryResult = MutableLiveData<Float?>()
    val expenseQueryResult: LiveData<Float?> get() = _expenseQueryResult
    private val _incomeQueryResult = MutableLiveData<Float?>()
    val incomeQueryResult: LiveData<Float?> get() = _incomeQueryResult

    fun getValueSum() {
        db.collection("users").document(userId).collection(collectionNameExpenses)
            .get().addOnSuccessListener {
                val number: MutableList<Expense>? = it.toObjects(Expense::class.java)
                if (number != null) {
                    for (i in 0 until number.size) {
                        expenseValueSum += number[i].value!!
                    }
                    _expenseQueryResult.postValue(expenseValueSum)
                }
            }
            .addOnFailureListener {
            }
        db.collection("users").document(userId).collection(collectionNameIncomes)
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
}
