package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Timer(private val onTick: (Int) -> Unit) {
    private val ONE_SECOND_MS = 1000L

    private var timerJob: Job? = null

    fun start(scope: CoroutineScope) {
        timerJob = scope.launch {
            while (isActive) {
                delay(ONE_SECOND_MS)
                onTick(1) // Increment time by 1 second
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
    }
}