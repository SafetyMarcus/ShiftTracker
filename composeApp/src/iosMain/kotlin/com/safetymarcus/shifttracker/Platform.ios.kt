package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDayCalendarUnit
import platform.Foundation.NSMonthCalendarUnit
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

actual fun currentMonth(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSMonthCalendarUnit, NSDate()).toInt() - 1 // 1-indexed -> 0-indexed to match android
}

actual fun currentDay(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSDayCalendarUnit, NSDate()).toInt() - 1 // 1-indexed -> 0-indexed to match android
}

@OptIn(ExperimentalForeignApi::class)
actual fun daysInMonth(month: Int): Int {
    val calendar = NSCalendar.currentCalendar
    calendar.maximumRangeOfUnit(NSDayCalendarUnit).useContents { return this.length.toInt() }
}