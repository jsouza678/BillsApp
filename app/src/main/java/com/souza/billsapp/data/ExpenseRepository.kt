package com.souza.billsapp.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseRepository {

    private var test = 1
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val query = db.collection("despesas")

    fun getData() : FirestoreRecyclerOptions<Expense> {
         return FirestoreRecyclerOptions
            .Builder<Expense>()
            .setQuery(query, Expense::class.java)
            .build()
    }

    fun insertData(data : Expense) {
        db.collection("despesas")
            .document((test++).toString())
            .set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

}

