package com.safetymarcus.shifttracker.upcoming

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.safetymarcus.shifttracker.models.Shift
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object UpcomingRepository : UpcomingContract.Repository, DefaultLifecycleObserver {

    override val upcomingShift = MutableStateFlow<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.coroutineScope.launch {
            upcomingShift.value = Firebase.firestore
                .collection("shifts")
                .get()
                .documents[0]
                .data(Shift.serializer())
        }
    }
}