package com.trio.service.tts

import android.speech.tts.TextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsQueueManager @Inject constructor(
    private val ttsEngine: TrioTtsEngine
) {
    private val mutex = Mutex()

    suspend fun speak(text: String, flush: Boolean = true) {
        withContext(Dispatchers.Main) {
            mutex.withLock {
                val mode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
                ttsEngine.speak(text, mode)
            }
        }
    }

    fun stop() {
        ttsEngine.stop()
    }
}
