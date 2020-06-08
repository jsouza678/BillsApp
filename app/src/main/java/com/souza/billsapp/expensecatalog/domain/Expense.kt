package com.souza.billsapp.expensecatalog.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.EMPTY_STRING

data class Expense(
    val value: Float? = null,
    val description: String? = null,
    @ServerTimestamp
    val date: Timestamp? = null,
    val wasPaid: Boolean = false,
    val imageUri: String = EMPTY_STRING
)
