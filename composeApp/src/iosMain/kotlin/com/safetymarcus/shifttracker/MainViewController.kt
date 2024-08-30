package com.safetymarcus.shifttracker

import androidx.compose.ui.window.ComposeUIViewController
import com.safetymarcus.shifttracker.upcoming.UpcomingRepository
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun MainViewController() = ComposeUIViewController {
    val model = UpcomingViewModel(UpcomingRepository(CoroutineScope(Dispatchers.Main)))
    App(model)
}