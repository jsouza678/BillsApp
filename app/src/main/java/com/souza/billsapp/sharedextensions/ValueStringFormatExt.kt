package com.souza.billsapp.sharedextensions

import android.util.Patterns
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

fun formatValueToCoin(number: Float): String {
    val df = DecimalFormat("#,###.00")
    df.roundingMode = RoundingMode.CEILING
    return ("R$ ${df.format(number)}")
}

fun isValidUrl(url: String): Boolean {
    val p: Pattern = Patterns.WEB_URL
    val m: Matcher = p.matcher(url.toLowerCase(Locale.getDefault()))
    return m.matches()
}
