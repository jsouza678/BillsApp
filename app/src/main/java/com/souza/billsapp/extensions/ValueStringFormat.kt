package com.souza.billsapp.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

fun formatValueToCoin(number: Float): String{
    val df = DecimalFormat("#,###.00")
    df.roundingMode = RoundingMode.CEILING
    return ("R$ ${df.format(number)}")
}
