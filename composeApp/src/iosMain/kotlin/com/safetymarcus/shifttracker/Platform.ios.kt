package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone

actual val Timestamp.dateString: String
    get() = NSDateFormatter.new()?.apply {
        setTimeZone(NSTimeZone.localTimeZone())
        setDateFormat("dd MMMM")
    }?.stringFromDate(Instant.fromEpochSeconds(this.seconds).toNSDate()) ?: ""

actual val Timestamp.timeString: String
    get() = NSDateFormatter.new()?.apply {
        setTimeZone(NSTimeZone.localTimeZone())
        setDateFormat("h:mm aa")
    }?.stringFromDate(Instant.fromEpochSeconds(this.seconds).toNSDate()) ?: ""
