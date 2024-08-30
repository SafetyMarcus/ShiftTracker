package com.safetymarcus.shifttracker

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.safetymarcus.shifttracker.components.NewShift
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
    var showingNewShift by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showingNewShift = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add shift")
            }
        }
    ) {
        Box {
            UpcomingShift(shift)
            if (showingNewShift)
                NewShift(
                    onConfirm = {
                        showingNewShift = false
                        model.addShift()
                    },
                    onDismiss = { showingNewShift = false }
                )
        }
    }
}