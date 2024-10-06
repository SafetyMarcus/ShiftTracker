package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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

suspend fun <T>  onIO(
    block: suspend CoroutineScope.() -> T
) = withContext(Dispatchers.IO, block)