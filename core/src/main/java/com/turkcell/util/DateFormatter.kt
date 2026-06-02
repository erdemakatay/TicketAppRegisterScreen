package com.turkcell.util

private val turkishMonthsShort = arrayOf(
    "Oca","Şub","Mar","Nis","May","Haz","Tem","Ağu","Eyl","Eki","Kas","Ara"
)

fun formatEventDate(isoDate: String): String {
    return try {
        val parts = isoDate.split("T")
        val dateParts = parts[0].split("-")
        val year = dateParts[0]
        val month = dateParts[1].toInt() - 1
        val day = dateParts[2]
        val time = parts[1].take(5)
        "$day ${turkishMonthsShort[month]} $year, $time"
    } catch (e: Exception) {
        isoDate.take(10)
    }
}