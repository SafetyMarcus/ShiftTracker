package com.safetymarcus.shifttracker.models

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val type: String,
    val startTime: Timestamp,
    val duration: Int,
)
