package com.souza.billsapp.incomecatalog.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.souza.billsapp.incomecatalog.utils.Constants.Companion.EMPTY_STRING

data class Income(
    val value: Float? = null,
    val description: String? = null,
    @ServerTimestamp
    val date: Timestamp? = null,
    val wasReceived: Boolean = false,
    val imageUri: String = EMPTY_STRING
)
