package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val Instant.timestamp get() = Timestamp.fromMilliseconds(toEpochMilliseconds().toDouble())

val LocalDateTime.timestamp get() = toInstant(TimeZone.currentSystemDefault()).timestamp

val Timestamp.localDateTime get() = toMilliseconds().localDateTime

val Long.localDateTime
    get() = Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

val Double.localDateTime get() = this.toLong().localDateTime

suspend fun <T> onIO(
    block: suspend CoroutineScope.() -> T
) = withContext(Dispatchers.IO, block)

//Ordinal for 0 index
fun currentMonthOrdinal() = Clock.System.now().toLocalDateTime(
    TimeZone.currentSystemDefault()
).month.ordinal


fun currentDay() = Clock.System.now().toLocalDateTime(
    TimeZone.currentSystemDefault()
).dayOfMonth - 1 //0 indexed

fun daysInMonth(month: Int) = with(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) {
    val year = if (month < (this.month.ordinal - 1)) year + 1 else year
    val nextMonth = month.nextMonth
    val nextYear = if (nextMonth == 0) year + 1 else year
    LocalDate(year, month, 1).daysUntil(LocalDate(nextYear, nextMonth, 1))
}

val Int.previousMonth get() = if(this == 0) 11 else this - 1

val Int.nextMonth get() = if(this == 11) 0 else this + 1