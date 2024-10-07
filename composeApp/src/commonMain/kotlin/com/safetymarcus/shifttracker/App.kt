package com.safetymarcus.shifttracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.safetymarcus.shifttracker.components.CalendarState
import com.safetymarcus.shifttracker.components.Medium
import com.safetymarcus.shifttracker.components.NewShift
import com.safetymarcus.shifttracker.components.UpcomingShift
import com.safetymarcus.shifttracker.core.MonthlyPagerState
import com.safetymarcus.shifttracker.core.MonthlyShifts
import com.safetymarcus.shifttracker.core.MonthlyShiftsState
import com.safetymarcus.shifttracker.core.rememberMonthlyShiftState
import com.safetymarcus.shifttracker.theme.AppTheme
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    model: UpcomingViewModel
) = AppTheme {
    val updating by remember { model.updating }
    val showingNewShift = remember { mutableStateOf(false) }
    val monthlyShiftsState = rememberMonthlyShiftState(
        currentMonthOrdinal = currentMonthOrdinal(),
        pagerState = MonthlyPagerState(currentPage = 1),
    )
    val currentCalendarState by remember { monthlyShiftsState.currentCalendarState }

    Scaffold(
        floatingActionButton = addShiftButton(updating, showingNewShift)
    ) {
        if (updating) Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        else Column(modifier = Modifier.fillMaxSize()) {
            ShiftHeader(model)
            MonthlyView(model = model, state = monthlyShiftsState)
        }
        NewShiftSelector(
            model = model,
            currentCalendarState = currentCalendarState,
            showingNewShiftState = showingNewShift,
        )
    }
}

private fun addShiftButton(
    updating: Boolean,
    showingNewShift: MutableState<Boolean>
) = @Composable {
    if (updating) Unit
    else FloatingActionButton(onClick = { showingNewShift.value = true }) {
        Icon(Icons.Filled.Add, contentDescription = "Add shift")
    }
}

@Composable
private fun ShiftHeader(model: UpcomingViewModel) {
    val shift by remember { model.upcoming }
    val updatingShift by remember { model.updatingNextShift }
    if (updatingShift) Loading() else UpcomingShift(shift)
}

@Composable
private fun NewShiftSelector(
    model: UpcomingViewModel,
    currentCalendarState: CalendarState,
    showingNewShiftState: MutableState<Boolean>,
) {
    val showingNewShift by remember { showingNewShiftState }
    val selectedMonth by remember(currentCalendarState) { derivedStateOf { currentCalendarState.monthOrdinal } }
    val selectedDay by remember(currentCalendarState) { derivedStateOf { currentCalendarState.selectedDay } }
    val dayInstant by remember(selectedMonth, currentCalendarState) {
        derivedStateOf {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            LocalDateTime(
                year = now.year,
                monthNumber = selectedMonth + 1, //calendar is 0 indexed
                dayOfMonth = selectedDay + 1, //calendar is 0 indexed
                hour = 0,
                minute = 0,
            ).toInstant(TimeZone.currentSystemDefault())
        }
    }
    if (showingNewShift) NewShift(
        date = dayInstant,
        onConfirm = { time, type ->
            showingNewShiftState.value = false
            time?.let { model.addShift(it, type) }
        },
        onDismiss = { showingNewShiftState.value = false }
    )
}

@Composable
private fun MonthlyView(
    model: UpcomingViewModel,
    state: MonthlyShiftsState,
) {
    val shifts = remember { model.shifts } //Todo populate shifts onto the calendar
    val updatingRecentShifts by remember { model.updatingRecentShifts }
    if (updatingRecentShifts) Loading() else MonthlyShifts(state)
}

@Composable
private fun Loading() = Row(Modifier.padding(Medium)) { CircularProgressIndicator() }