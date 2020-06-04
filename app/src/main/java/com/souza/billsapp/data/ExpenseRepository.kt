package com.souza.billsapp.data

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class ExpenseRepository {

    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private val today = calendar.time
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val query = db.collection("despesas")
    private val queryFilteredByMonth = db.collection("despesas").whereGreaterThanOrEqualTo("date", today)

    fun getData() : FirestoreRecyclerOptions<Expense> {
         return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(query, Expense::class.java)
            .build()
    }

    fun getMonthlyData() : FirestoreRecyclerOptions<Expense> {
        return FirestoreRecyclerOptions
                .Builder<Expense>()
                .setQuery(queryFilteredByMonth, Expense::class.java)
                .build()
    }

    fun insertData(data : Expense) {
        db.collection("despesas")
            .document()
            .set(data)
            .addOnSuccessListener {
                //TODO
                }
            .addOnFailureListener {
                //TODO
                }
    }

    fun updateData(data : Expense, document: String) {
        db.collection("despesas")
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

