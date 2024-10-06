package com.safetymarcus.shifttracker.models

import com.safetymarcus.shifttracker.localDateTime
import com.safetymarcus.shifttracker.timestamp
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val startTime: Timestamp = Timestamp.now(),
    val duration: Int = 510,
    var _type: String
) {
    val type get() = ShiftType.valueOf(_type)

    val month get() = startTime.localDateTime.monthNumber
}

enum class ShiftType(
    val startTime: Int?,
) {
    EARLY(
        startTime = 6
    ),
    LATE(
        startTime = 14
    ),
    NIGHT(
        startTime = 21
    ),
    CUSTOM(
        startTime = null
    );

    fun getTimestamp(
        millis: Long,
        startTime: Int?
    ) = with(millis.localDateTime) {
        LocalDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hour = startTime ?: this@ShiftType.startTime ?: 0,
            minute = 0,
            second = 0
        ).timestamp
    }
}