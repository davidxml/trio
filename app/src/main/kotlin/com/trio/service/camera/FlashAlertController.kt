package com.trio.service.camera

import android.app.Notification
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashAlertController @Inject constructor(
    private val torchManager: CameraTorchManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var activeSequence: Job? = null

    fun flashForNotification(importance: Int) {
        if (!torchManager.isTorchAvailable) return

        val (onMs, offMs, count) = when {
            importance >= Notification.IMPORTANCE_HIGH -> Triple(50L, 50L, 5)
            importance >= Notification.IMPORTANCE_DEFAULT -> Triple(100L, 100L, 3)
            else -> Triple(200L, 0L, 1)
        }

        activeSequence?.cancel()
        activeSequence = scope.launch {
            executeFlashSequence(onMs, offMs, count)
        }
    }

    private suspend fun executeFlashSequence(onMs: Long, offMs: Long, count: Int) {
        repeat(count) {
            if (!torchManager.isTorchAvailable) return
            torchManager.turnOn()
            delay(onMs)
            torchManager.turnOff()
            if (offMs > 0 && it < count - 1) delay(offMs)
        }
    }

    fun stopAll() {
        activeSequence?.cancel()
        activeSequence = null
        scope.launch {
            torchManager.turnOff()
        }
    }

    companion object {
        private const val TAG = "FlashAlertController"
    }
}
