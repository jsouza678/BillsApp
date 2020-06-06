package com.souza.billsapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Expense(
    val value: Int? = null,
    val description: String? = null,
    @ServerTimestamp
    val date : Timestamp? = null,
    val wasPaid: Boolean = false,
    val imageUri: String = ""
)