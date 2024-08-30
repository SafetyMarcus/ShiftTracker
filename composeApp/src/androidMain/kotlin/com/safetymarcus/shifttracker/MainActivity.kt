package com.safetymarcus.shifttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.safetymarcus.shifttracker.upcoming.UpcomingRepository
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel

class MainActivity : ComponentActivity() {

    private val model by viewModels<UpcomingViewModel> {
        FirebaseApp.initializeApp(this)
        UpcomingViewModel.Factory(UpcomingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(UpcomingRepository)
        lifecycle.addObserver(model)
        setContent { App(model) }
    }
}