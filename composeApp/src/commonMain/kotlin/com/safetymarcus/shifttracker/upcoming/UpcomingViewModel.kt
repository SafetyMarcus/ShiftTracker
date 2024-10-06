package com.safetymarcus.shifttracker.upcoming

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.safetymarcus.shifttracker.core.JobRegister
import com.safetymarcus.shifttracker.core.LifecycleAwareViewModel
import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.models.ShiftType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class UpcomingViewModel(
    private val repository: UpcomingContract.Repository
) : LifecycleAwareViewModel() {

    val updating = mutableStateOf(false)
    val updatingNextShift = mutableStateOf(false)
    val updatingRecentShifts = mutableStateOf(false)
    val upcoming = mutableStateOf<Shift?>(null)
    val shifts = mutableStateMapOf<Int, List<Shift>>()

    private val jobs = arrayListOf<Job>()

    override fun JobRegister.registerJobs() {
        register { repository.updatingNextShift.collect { updatingNextShift.value = it } }
        register { repository.updatingRecentShifts.collect { updatingRecentShifts.value = it } }
        register { repository.updating.collect { updating.value = it } }
        register { repository.upcomingShift.collect { upcoming.value = it } }
        register { repository.shifts.collect { shifts.putAll(it) } }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        jobs.forEach { it.cancel() }
    }

    fun addShift(
        millis: Long,
        shiftType: ShiftType,
    ) = viewModelScope.launch {
        repository.addShift(
            millis = millis,
            shiftType = shiftType,
        )
    }

    //factory
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: UpcomingContract.Repository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras) =
            UpcomingViewModel(repository) as T
    }
}