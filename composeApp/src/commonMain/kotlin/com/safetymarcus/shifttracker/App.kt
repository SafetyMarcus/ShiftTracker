package com.safetymarcus.shifttracker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.safetymarcus.shifttracker.components.CalendarState
import com.safetymarcus.shifttracker.components.Medium
import com.safetymarcus.shifttracker.components.Month
import com.safetymarcus.shifttracker.components.NewShift
import com.safetymarcus.shifttracker.components.UpcomingShift
import com.safetymarcus.shifttracker.theme.AppTheme
import com.safetymarcus.shifttracker.upcoming.UpcomingViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App(
    model: UpcomingViewModel
) = AppTheme {
    val shift by remember { model.upcoming }
    val updating by remember { model.updating }
    val updatingShift by remember { model.updatingNextShift }
    val updatingRecentShifts by remember { model.updatingRecentShifts }
    val shifts = remember { model.shifts } //Todo populate shifts onto the calendar
    var showingNewShift by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(currentMonthOrdinal()) }
    val calendarStates = remember {
        mutableStateListOf(
            CalendarState(monthOrdinal = selectedMonth.previousMonth, selectedDay = 0),
            CalendarState(monthOrdinal = selectedMonth, selectedDay = currentDay()),
            CalendarState(monthOrdinal = selectedMonth.nextMonth, selectedDay = 0)
        )
    }
    val currentCalendarState by remember {
        derivedStateOf {
            calendarStates.first { it.monthOrdinal == selectedMonth }
        }
    }

    val dayInstant by remember(selectedMonth, currentCalendarState) {
        derivedStateOf {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            LocalDateTime(
                year = now.year,
                monthNumber = selectedMonth + 1, //calendar is 0 indexed
                dayOfMonth = calendarStates.first { it.monthOrdinal == selectedMonth }.selectedDay + 1, //calendar is 0 indexed
                hour = 0,
                minute = 0,
                second = 0
            ).toInstant(TimeZone.currentSystemDefault())
        }
    }
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 },
    )
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedMonth = shifts.keys.elementAt(page)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (updating) return@Scaffold
            FloatingActionButton(onClick = { showingNewShift = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add shift")
            }
        }
    ) {
        if (updating) Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        else Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (updatingShift) Loading() else UpcomingShift(shift)
            if (updatingRecentShifts) Loading() else HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pagerState,
            ) { Month(calendarStates[it]) }
        }
        if (showingNewShift) NewShift(
            date = dayInstant,
            onConfirm = { time, type ->
                showingNewShift = false
                time?.let { model.addShift(it, type) }
            },
            onDismiss = { showingNewShift = false }
        )
    }
}

@Composable
private fun Loading() = Row(Modifier.padding(Medium)) { CircularProgressIndicator() }