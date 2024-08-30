package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual val Timestamp.dateString: String
    get() = SimpleDateFormat("dd\nMMMM", Locale.ENGLISH).format(Date(this.seconds * 1000))

actual val Timestamp.timeString: String
    get() = SimpleDateFormat("HH:mm aa", Locale.ENGLISH).format(Date(this.seconds * 1000))