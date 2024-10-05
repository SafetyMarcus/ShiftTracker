package com.safetymarcus.shifttracker.upcoming

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.models.ShiftType
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object UpcomingRepository : UpcomingContract.Repository, DefaultLifecycleObserver {

    override val updating = MutableStateFlow(false)
    override val upcomingShift = MutableStateFlow<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updating.value = true
        owner.lifecycle.coroutineScope.launch {
            Firebase.firestore
                .collection("shifts")
                .orderBy("startTime", Direction.ASCENDING)
                .where { "startTime" greaterThan Timestamp.now() }
                .limit(1)
                .snapshots()
                .collect {
                    upcomingShift.value = it.documents.firstOrNull()?.data(Shift.serializer())
                    updating.value = false
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