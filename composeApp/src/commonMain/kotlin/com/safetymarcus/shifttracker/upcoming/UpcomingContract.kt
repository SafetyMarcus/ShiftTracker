package com.safetymarcus.shifttracker.upcoming

import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.models.ShiftType
import kotlinx.coroutines.flow.MutableStateFlow

interface UpcomingContract {
    interface Repository {
        val updating: MutableStateFlow<Boolean>
        val upcomingShift: MutableStateFlow<Shift?>
        suspend fun addShift(
            millis: Long,
            shiftType: ShiftType
        )
    }
}
