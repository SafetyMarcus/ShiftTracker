package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone

actual val Timestamp.dateString: String
    get() = NSDateFormatter.new()?.apply {
        setTimeZone(NSTimeZone.localTimeZone())
        setDateFormat("dd MMMM")
    }?.stringFromDate(NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble())) ?: ""

actual val Timestamp.timeString: String
    get() = NSDateFormatter.new()?.apply {
        setTimeZone(NSTimeZone.localTimeZone())
        setDateFormat("HH:mm aa")
    }?.stringFromDate(NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble())) ?: ""