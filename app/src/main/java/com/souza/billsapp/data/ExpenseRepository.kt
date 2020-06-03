package com.souza.billsapp.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseRepository {


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

