package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp

expect val Timestamp.dateString: String
expect val Timestamp.timeString: String

expect fun currentMonth(): Int

expect fun daysInMonth(month: Int): Int