package com.safetymarcus.shifttracker.upcoming

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.models.ShiftType
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class UpcomingViewModel(
    private val repository: UpcomingContract.Repository
) : ViewModel(), DefaultLifecycleObserver {

    val updating = mutableStateOf(false)
    val upcoming = mutableStateOf<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        repository.updating
        viewModelScope.launch {
            repository.updating.collect { updating.value = it }
        }
        viewModelScope.launch {
            repository.upcomingShift.collect { upcoming.value = it }
        }
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