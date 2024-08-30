package com.safetymarcus.shifttracker.models

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val type: String = "early",
    val startTime: Timestamp = Timestamp.now(),
    val duration: Int = 510,
)
