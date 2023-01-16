package com.example.screen_resources

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Loop {

    private var _isLoop = false

    fun start(
        waitMs: Long = 300L,
        coroutineScope: CoroutineScope,
        destinationFunction: () -> Unit
    ) {
        _isLoop = true
        coroutineScope.launch {
            while (_isLoop) {
                destinationFunction()
                delay(waitMs)
            }
        }
    }

    fun stop() {
        _isLoop = false
    }
}