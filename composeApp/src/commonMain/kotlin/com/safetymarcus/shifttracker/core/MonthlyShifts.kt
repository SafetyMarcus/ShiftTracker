package com.safetymarcus.shifttracker.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.safetymarcus.shifttracker.components.CalendarState
import com.safetymarcus.shifttracker.components.Month
import com.safetymarcus.shifttracker.currentDay
import com.safetymarcus.shifttracker.nextMonth
import com.safetymarcus.shifttracker.previousMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthlyShifts(
    state: MonthlyShiftsState
) = HorizontalPager(
    modifier = Modifier.fillMaxSize(),
    verticalAlignment = Alignment.Top,
    state = state.pagerState,
) { Month(state.calendarStates[it]) }

@Composable
fun rememberMonthlyShiftState(
    currentMonthOrdinal: Int,
    pagerState: MonthlyPagerState,
) = rememberSaveable(saver = MonthlyShiftsState.Saver()) {
    MonthlyShiftsState(
        calendarStates = listOf(
            CalendarState(monthOrdinal = currentMonthOrdinal.previousMonth, selectedDay = 0),
            CalendarState(monthOrdinal = currentMonthOrdinal, selectedDay = currentDay()),
            CalendarState(monthOrdinal = currentMonthOrdinal.nextMonth, selectedDay = 0)
        ),
        pagerState = pagerState
    )
}

class MonthlyShiftsState(
    calendarStates: List<CalendarState>,
    val pagerState: MonthlyPagerState,
) {
    val calendarStates = mutableStateListOf(*calendarStates.toTypedArray())
    val currentCalendarState = derivedStateOf { calendarStates[pagerState.currentPage] }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun Saver(): Saver<MonthlyShiftsState, Any> = listSaver(
            save = {
                val saved = arrayListOf<Int>()
                saved.add(it.pagerState.currentPage)
                it.calendarStates.forEach {
                    saved.add(it.monthOrdinal)
                    saved.add(it.selectedDay)
                }
                saved
            },
            restore = {
                val pagerState = MonthlyPagerState(currentPage = it[0])
                val calendarStates = it.drop(1).chunked(2).map { (month, day) ->
                    CalendarState(monthOrdinal = month, selectedDay = day)
                }
                MonthlyShiftsState(
                    calendarStates = calendarStates,
                    pagerState = pagerState,
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
class MonthlyPagerState(currentPage: Int) : PagerState(
    currentPage = currentPage,
) { override val pageCount: Int = 3 }