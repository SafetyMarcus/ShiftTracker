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
import dev.gitlive.firebase.firestore.fromMilliseconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object UpcomingRepository : UpcomingContract.Repository, DefaultLifecycleObserver {

    override val updating = MutableStateFlow(false)
    override val upcomingShift = MutableStateFlow<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.coroutineScope.launch {
            Firebase.firestore
                .collection("shifts")
                .orderBy("startTime", Direction.ASCENDING)
                .where { "startTime" greaterThan Timestamp.now() }
                .limit(1)
                .snapshots()
                .collect {
                    upcomingShift.value = it.documents.firstOrNull()?.data(Shift.serializer())
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
                    startTime = Timestamp.fromMilliseconds(millis.toDouble()),
                    _type = shiftType.name,
                )
            )
    }
}