package com.souza.billsapp.result.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.result.domain.repository.ResultRepository
import com.souza.billsapp.result.utils.Constants.Companion.ABOLUTE_ZERO
import com.souza.billsapp.result.utils.Constants.Companion.FLOAT_ZERO_ABSOLUTE
import com.souza.billsapp.result.utils.Constants.Companion.USERS_TABLE_NAME
import java.util.Calendar

class ResultRepositoryImpl : ResultRepository {

    private val auth: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val calendar: Calendar = Calendar.getInstance()
    private val mes: Int = calendar.get(Calendar.MONTH)
    private val ano: Int = calendar.get(Calendar.YEAR)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = "$auth"
    private var expenseValueSum: Float = FLOAT_ZERO_ABSOLUTE
    private var incomesValueSum: Float = FLOAT_ZERO_ABSOLUTE
    private val collectionNameExpenses: String = "despesas_$mes" + "_" + "$ano"
    private val collectionNameIncomes: String = "receita_$mes" + "_" + "$ano"
    private val _expenseQueryResult = MutableLiveData<Float?>()
    val expenseQueryResult: LiveData<Float?> get() = _expenseQueryResult
    private val _incomeQueryResult = MutableLiveData<Float?>()
    val incomeQueryResult: LiveData<Float?> get() = _incomeQueryResult

    override fun getValueSum() {
        expenseValueSum = FLOAT_ZERO_ABSOLUTE
        incomesValueSum = FLOAT_ZERO_ABSOLUTE
        db.collection(USERS_TABLE_NAME).document(userId).collection(collectionNameExpenses)
            .get().addOnSuccessListener {
                val number: MutableList<Expense>? = it.toObjects(
                    Expense::class.java)
                if (number != null) {
                    for (i in ABOLUTE_ZERO until number.size) {
                        expenseValueSum += number[i].value!!
                    }
                    _expenseQueryResult.postValue(expenseValueSum)
                }
            }
            .addOnFailureListener {
            }
        db.collection(USERS_TABLE_NAME).document(userId).collection(collectionNameIncomes)
            .get().addOnSuccessListener {
                val number: MutableList<Income>? = it.toObjects(
                    Income::class.java)
                if (number != null) {
                    for (i in ABOLUTE_ZERO until number.size) {
                        incomesValueSum += number[i].value!!
                    }
                    _incomeQueryResult.postValue(incomesValueSum)
                }
            }
            .addOnFailureListener { }
    }
}
