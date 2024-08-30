package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle

actual val Timestamp.dateString: String
    get() = NSDateFormatter.new()?.apply {
        setDateFormat("dd MMMM")
    }?.stringFromDate(NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble()))  ?: ""

actual val Timestamp.timeString: String
    get() = NSDateFormatter.localizedStringFromDate(
        date = NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble()),
        dateStyle = NSDateFormatterNoStyle,
        timeStyle = NSDateFormatterShortStyle
    )