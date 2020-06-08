package com.souza.billsapp.sharedextensions

import java.text.Format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateWithSeconds(date: Date?): String {
    val formatter: Format = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}

fun formatDate(date: Date?): String {
    val formatter: Format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}
