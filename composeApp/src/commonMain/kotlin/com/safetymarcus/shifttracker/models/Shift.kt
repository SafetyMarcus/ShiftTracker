package com.safetymarcus.shifttracker.models

import dev.gitlive.firebase.firestore.Timestamp
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
    startTime: Int?,
) {
    EARLY(
        startTime = 6
    ), LATE(
        startTime = 14
    ), NIGHT(
        startTime = 21
    ), CUSTOM(
        startTime = null
    )
}