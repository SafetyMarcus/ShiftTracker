package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

actual val Timestamp.dateString: String
    get() = SimpleDateFormat("dd\nMMMM", Locale.ENGLISH).format(Date(this.seconds * 1000))

actual val Timestamp.timeString: String
    get() = SimpleDateFormat("HH:mm aa", Locale.ENGLISH).format(Date(this.seconds * 1000))

val calendar = GregorianCalendar()

actual fun currentMonth(): Int = calendar.apply { time = Date() }.get(GregorianCalendar.MONTH)

actual fun currentDay(): Int = calendar.apply { time = Date() }.get(GregorianCalendar.DAY_OF_MONTH)

actual fun daysInMonth(month: Int): Int = calendar.apply {
    set(GregorianCalendar.MONTH, month)
}.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)