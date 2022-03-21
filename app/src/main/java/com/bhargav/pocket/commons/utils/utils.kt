package com.bhargav.pocket.commons.utils

import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// CONSTANTS
val innerPadding = 24.dp


// FUNCTIONS
fun randomId() = UUID.randomUUID().toString().replace("-", "")

fun toCurrency(amount: Float): String = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)

fun getTime(date: Long): String = SimpleDateFormat("hh:mm a", Locale("en", "IN")).format(date).uppercase()

fun getDate(date: Long, pattern: String = "dd MMM, yy"): String =
    SimpleDateFormat(pattern, Locale("en", "IN")).format(date)
