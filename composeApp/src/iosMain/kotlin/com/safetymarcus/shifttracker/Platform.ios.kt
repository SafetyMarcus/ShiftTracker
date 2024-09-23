package com.safetymarcus.shifttracker

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
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
    }?.stringFromDate(NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble())) ?: ""

actual val Timestamp.timeString: String
    get() = NSDateFormatter.new()?.apply {
        setTimeZone(NSTimeZone.localTimeZone())
        setDateFormat("HH:mm aa")
    }?.stringFromDate(NSDate(timeIntervalSinceReferenceDate = this.seconds.toDouble())) ?: ""

actual fun currentMonth(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSMonthCalendarUnit, NSDate()).toInt()
}

actual fun currentDay(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSDayCalendarUnit, NSDate()).toInt()
}

@OptIn(ExperimentalForeignApi::class)
actual fun daysInMonth(month: Int): Int {
    val calendar = NSCalendar.currentCalendar
    calendar.maximumRangeOfUnit(NSDayCalendarUnit).useContents { return this.length.toInt() }
}