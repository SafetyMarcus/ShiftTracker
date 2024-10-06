package com.safetymarcus.shifttracker.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JobRegister {
    val jobs = mutableListOf<Job>()

    private var scope: CoroutineScope? = null

    fun attach(scope: CoroutineScope) {
        this.scope = scope
    }

    fun detach() {
        cancelAll()
        scope = null
    }

    fun register(jobCreator: suspend CoroutineScope.() -> Job) {
        scope?.launch { jobs.add(jobCreator()) }
    }

    fun cancelAll() = jobs.forEach { job ->
        job.cancel()
        jobs.remove(job)
    }
}