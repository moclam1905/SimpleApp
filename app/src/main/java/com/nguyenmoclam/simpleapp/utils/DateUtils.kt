package com.nguyenmoclam.simpleapp.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun formatDate(dateStr: String, dateFormatPattern: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())

    val date = inputFormat.parse(dateStr)
    return date?.let { outputFormat.format(it) } ?: dateStr
}