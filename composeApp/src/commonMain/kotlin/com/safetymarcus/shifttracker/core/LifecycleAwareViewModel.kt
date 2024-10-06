package com.safetymarcus.shifttracker.core

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

open class LifecycleAwareViewModel : ViewModel(), DefaultLifecycleObserver {

    private val jobRegister = JobRegister()
    open fun JobRegister.registerJobs() {}

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        jobRegister.attach(viewModelScope)
    }

    override fun onResume(owner: LifecycleOwner) {
        jobRegister.registerJobs()
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        jobRegister.cancelAll()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        jobRegister.detach()
        super.onDestroy(owner)
    }
}