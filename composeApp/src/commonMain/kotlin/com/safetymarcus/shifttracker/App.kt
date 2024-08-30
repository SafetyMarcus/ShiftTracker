package com.safetymarcus.shifttracker

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.safetymarcus.shifttracker.components.UpcomingShift
import com.safetymarcus.shifttracker.theme.AppTheme
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    model: UpcomingViewModel
) = AppTheme {
    val shift by remember { model.upcoming }
    Scaffold { UpcomingShift(shift) }
}