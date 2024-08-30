package com.safetymarcus.shifttracker.upcoming

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.safetymarcus.shifttracker.models.Shift
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object UpcomingRepository : UpcomingContract.Repository, DefaultLifecycleObserver {

    override val updating = MutableStateFlow(false)
    override val upcomingShift = MutableStateFlow<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.coroutineScope.launch {
            upcomingShift.value = Firebase.firestore
                .collection("shifts")
                .orderBy("startTime", Direction.DESCENDING)
                .limit(1)
                .get()
                .documents.firstOrNull()
                ?.data(Shift.serializer())
        }
    }

    override suspend fun addShift() {
        Firebase.firestore.collection("shifts").add(Shift())
    }
}