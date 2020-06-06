package com.souza.billsapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Income(
    val value: Int? = null,
    val description: String? = null,
    @ServerTimestamp
    val date : Timestamp? = null,
    val wasReceived: Boolean = false,
    val imageUri: String? = null
)