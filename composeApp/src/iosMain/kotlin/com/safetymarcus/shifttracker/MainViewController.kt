@file:Suppress("unused")

package com.safetymarcus.shifttracker

import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.safetymarcus.shifttracker.upcoming.UpcomingRepository
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel

fun MainViewController() = ComposeUIViewController {
    val model = UpcomingViewModel(UpcomingRepository)
    LocalLifecycleOwner.current.lifecycle.addObserver(UpcomingRepository)
    LocalLifecycleOwner.current.lifecycle.addObserver(model)
    App(model)
}