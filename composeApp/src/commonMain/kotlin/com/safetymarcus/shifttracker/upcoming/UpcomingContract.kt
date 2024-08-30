package com.safetymarcus.shifttracker.upcoming

import com.safetymarcus.shifttracker.models.Shift
import kotlinx.coroutines.flow.MutableStateFlow

interface UpcomingContract {
    interface Repository {
        val upcomingShift: MutableStateFlow<Shift?>
    }
}