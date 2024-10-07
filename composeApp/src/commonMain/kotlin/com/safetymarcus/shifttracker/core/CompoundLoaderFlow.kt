package com.safetymarcus.shifttracker.core

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * AND style combined loader. If one is false all are false.
 */
class CompoundLoaderFlow(
    vararg flows: StateFlow<Boolean>
) : MutableStateFlow<Boolean> by MutableStateFlow(false) {

    private val flows = flows.toList()

    override suspend fun collect(collector: FlowCollector<Boolean>): Nothing {
        flows.forEach { it.collect { collector.emit(value) } }
        throw IllegalStateException("Should never reach here")
    }

    override var value: Boolean
        get() = flows.all { it.value }
        set(_) {
            throw UnsupportedOperationException("Cannot set value of CompoundLoaderFlow")
        }
}