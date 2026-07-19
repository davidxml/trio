package com.trio.presentation.launcher.components.vision

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.trio.service.tts.TtsQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TtsAnnouncer(private val ttsManager: TtsQueueManager) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun announce(text: String, flush: Boolean = true) {
        scope.launch {
            ttsManager.speak(text, flush)
        }
    }

    fun stop() {
        ttsManager.stop()
    }

    fun destroy() {
        scope.cancel()
    }
}

@Composable
fun rememberTtsAnnouncer(ttsManager: TtsQueueManager): TtsAnnouncer {
    val announcer = remember(ttsManager) { TtsAnnouncer(ttsManager) }
    DisposableEffect(Unit) {
        onDispose {
            announcer.stop()
            announcer.destroy()
        }
    }
    return announcer
}
