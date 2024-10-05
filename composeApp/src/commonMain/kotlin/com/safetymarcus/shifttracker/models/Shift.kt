package com.safetymarcus.shifttracker.models

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val startTime: Timestamp = Timestamp.now(),
    val duration: Int = 510,
    var _type: String
) {
    val type get() = ShiftType.valueOf(_type)
}

enum class ShiftType(
    val startTime: Int?,
) {
    EARLY(
        startTime = 6
    ), LATE(
        startTime = 14
    ), NIGHT(
        startTime = 21
    ), CUSTOM(
        startTime = null
    );

    fun getTimestamp(
        millis: Long,
        startTime: Int?
    ): Timestamp {
        val start = startTime ?: this.startTime ?: 0
        val shift = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val localTime = LocalDateTime(shift.year, shift.monthNumber, shift.dayOfMonth, start, 0, 0)
        val instant = localTime.toInstant(TimeZone.currentSystemDefault())
        return Timestamp.fromMilliseconds(instant.toEpochMilliseconds().toDouble())
    }
}