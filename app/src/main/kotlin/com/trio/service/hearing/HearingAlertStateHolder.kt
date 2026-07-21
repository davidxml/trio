package com.trio.service.hearing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.withLock

data class CaptionEntry(
    val text: String,
    val source: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    val textHash: Int = text.hashCode()
}

data class AlertEvent(
    val title: String,
    val body: String,
    val importance: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Singleton
class HearingAlertStateHolder @Inject constructor() {

    private val captionLock = ReentrantLock()
    private val alertLock = ReentrantLock()

    private val _recentCaptions = MutableStateFlow<List<CaptionEntry>>(emptyList())
    val recentCaptions: StateFlow<List<CaptionEntry>> = _recentCaptions.asStateFlow()

    private val _pendingAlerts = MutableStateFlow<List<AlertEvent>>(emptyList())
    val pendingAlerts: StateFlow<List<AlertEvent>> = _pendingAlerts.asStateFlow()

    private val _soundLevel = MutableStateFlow(0f)
    val soundLevel: StateFlow<Float> = _soundLevel.asStateFlow()

    fun pushCaption(text: String, source: String) {
        captionLock.withLock {
            val now = System.currentTimeMillis()
            val entry = CaptionEntry(text = text, source = source, timestamp = now)
            val current = _recentCaptions.value
            val deduped = current.filter {
                it.textHash != entry.textHash && now - it.timestamp < CAPTION_TTL_MS
            }
            _recentCaptions.value = listOf(entry) + deduped.take(MAX_CAPTIONS - 1)
        }
    }

    fun pushAlert(title: String, body: String, importance: Int) {
        alertLock.withLock {
            val now = System.currentTimeMillis()
            val event = AlertEvent(title = title, body = body, importance = importance, timestamp = now)
            val current = _pendingAlerts.value
            val deduped = current.filter {
                it.title != event.title && now - it.timestamp < ALERT_TTL_MS
            }
            _pendingAlerts.value = listOf(event) + deduped.take(MAX_ALERTS - 1)
        }
    }

    fun dismissAlert(index: Int) {
        alertLock.withLock {
            val current = _pendingAlerts.value.toMutableList()
            if (index in current.indices) {
                current.removeAt(index)
                _pendingAlerts.value = current
            }
        }
    }

    fun dismissAlertByTitle(title: String) {
        alertLock.withLock {
            _pendingAlerts.value = _pendingAlerts.value.filter { it.title != title }
        }
    }

    fun updateSoundLevel(level: Float) {
        _soundLevel.value = level.coerceIn(0f, 1f)
    }

    companion object {
        private const val MAX_CAPTIONS = 20
        private const val MAX_ALERTS = 10
        private const val ALERT_TTL_MS = 60_000L
        private const val CAPTION_TTL_MS = 30_000L
    }
}
