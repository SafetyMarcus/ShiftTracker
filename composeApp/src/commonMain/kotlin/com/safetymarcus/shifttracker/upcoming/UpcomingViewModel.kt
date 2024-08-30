package com.safetymarcus.shifttracker.upcoming

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.safetymarcus.shifttracker.models.Shift
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class UpcomingViewModel(
    private val repository: UpcomingContract.Repository
) : ViewModel(), DefaultLifecycleObserver {

    val upcoming = mutableStateOf<Shift?>(null)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch { repository.upcomingShift.collect { upcoming.value = it } }
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