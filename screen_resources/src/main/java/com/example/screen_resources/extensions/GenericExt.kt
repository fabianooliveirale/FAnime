package com.example.screen_resources.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun startLoop(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: () -> Unit
) {
    coroutineScope.launch {
        while (true) {
            destinationFunction()
            delay(waitMs)
        }
    }
}

