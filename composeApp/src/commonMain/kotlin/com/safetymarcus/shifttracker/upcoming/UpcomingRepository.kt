package com.safetymarcus.shifttracker.upcoming

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.safetymarcus.shifttracker.core.CompoundLoaderFlow
import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.models.ShiftType
import com.safetymarcus.shifttracker.onIO
import com.safetymarcus.shifttracker.timestamp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object UpcomingRepository : UpcomingContract.Repository, DefaultLifecycleObserver {

    override val updatingNextShift = MutableStateFlow(true)
    override val updatingRecentShifts = MutableStateFlow(true)
    override val updating = CompoundLoaderFlow(updatingNextShift, updatingRecentShifts)
    override val upcomingShift = MutableStateFlow<Shift?>(null)
    override val shifts = MutableStateFlow<Map<Int, List<Shift>>>(emptyMap())

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.registerForNextShift()
        val (queryStart, queryEnd) = queryDuration()
        owner.registerForRecentShifts(queryStart, queryEnd)
    }

    private fun LifecycleOwner.registerForNextShift() =
        lifecycle.coroutineScope.launch {
            onIO {
                Firebase.firestore
                    .collection("shifts")
                    .orderBy("startTime", Direction.ASCENDING)
                    .where { "startTime" greaterThan Timestamp.now() }
                    .limit(1)
                    .snapshots()
                    .collect {
                        upcomingShift.value = it.documents.firstOrNull()?.data(Shift.serializer())
                        updatingNextShift.value = false
                    }
            }
        }

    private fun queryDuration(): Pair<Instant, Instant> {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfMonth = Clock.System.now()
            .toLocalDateTime(timeZone)
            .let { LocalDateTime(it.year, it.monthNumber, 1, 0, 0, 0) }
            .toInstant(timeZone)
        //Start of previous month to end of next month
        return startOfMonth.minus(1, DateTimeUnit.MONTH, timeZone) to
                startOfMonth.plus(2, DateTimeUnit.MONTH, timeZone)
                    .minus(1, DateTimeUnit.DAY, timeZone)
    }

    private fun LifecycleOwner.registerForRecentShifts(
        queryStart: Instant,
        queryEnd: Instant
    ) = lifecycle.coroutineScope.launch {
        onIO {
            Firebase.firestore
                .collection("shifts")
                .orderBy("startTime", Direction.ASCENDING)
                .where { "startTime" greaterThan queryStart.timestamp }
                .where { "startTime" lessThan queryEnd.timestamp }
                .snapshots()
                .collect {
                    shifts.value = it.documents
                        .map { document -> document.data(Shift.serializer()) }
                        .groupBy { shift -> shift.month }
                    updatingRecentShifts.value = false
                }
        }
    }

    override suspend fun addShift(
        millis: Long,
        shiftType: ShiftType
    ) {
        Firebase.firestore
            .collection("shifts")
            .add(
                Shift(
                    startTime = shiftType.getTimestamp(millis, shiftType.startTime),
                    _type = shiftType.name,
                )
            )
    }
}