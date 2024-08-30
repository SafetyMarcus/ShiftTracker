package com.safetymarcus.shifttracker

import android.os.Build
import dev.gitlive.firebase.firestore.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val Timestamp.dateString: String
    get() = SimpleDateFormat("dd\nMMMM", Locale.ENGLISH).format(Date(this.seconds * 1000))

actual val Timestamp.timeString: String
    get() = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(this.seconds * 1000))