package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val Timestamp.dateString: String
expect val Timestamp.timeString: String
