package com.safetymarcus.shifttracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import com.safetymarcus.shifttracker.components.Month
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
    //TODO migrate to monthly list with with default month selection driven through VM
    val currentMonth by remember { mutableStateOf(currentMonth()) }
    val currentDay by remember { mutableStateOf(currentDay()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showingNewShift = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add shift")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { UpcomingShift(shift) }
            item {
                Month(
                    month = currentMonth,
                    today = currentDay
                )
            }
        }
        if (showingNewShift) NewShift(
            onConfirm = {
                showingNewShift = false
                model.addShift()
            },
            onDismiss = { showingNewShift = false }
        )
    }
}