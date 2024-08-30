package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val Timestamp.dateString: String
    get() = NSDateFormatter.localizedStringFromDate(
        date = NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble()),
        dateStyle = NSDateFormatterMediumStyle,
        timeStyle = NSDateFormatterNoStyle
    )

actual val Timestamp.timeString: String
    get() = NSDateFormatter.localizedStringFromDate(
        date = NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble()),
        dateStyle = NSDateFormatterNoStyle,
        timeStyle = NSDateFormatterMediumStyle
    )