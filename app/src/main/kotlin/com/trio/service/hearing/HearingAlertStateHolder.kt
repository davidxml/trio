package com.trio.service.hearing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class CaptionEntry(
    val text: String,
    val source: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class AlertEvent(
    val title: String,
    val body: String,
    val importance: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Singleton
class HearingAlertStateHolder @Inject constructor() {

    private val _recentCaptions = MutableStateFlow<List<CaptionEntry>>(emptyList())
    val recentCaptions: StateFlow<List<CaptionEntry>> = _recentCaptions.asStateFlow()

    private val _pendingAlerts = MutableStateFlow<List<AlertEvent>>(emptyList())
    val pendingAlerts: StateFlow<List<AlertEvent>> = _pendingAlerts.asStateFlow()

    fun pushCaption(text: String, source: String) {
        val entry = CaptionEntry(text = text, source = source)
        _recentCaptions.value = listOf(entry) + _recentCaptions.value
            .filter { System.currentTimeMillis() - it.timestamp < CAPTION_TTL_MS }
            .take(MAX_CAPTIONS - 1)
    }

    fun pushAlert(title: String, body: String, importance: Int) {
        val event = AlertEvent(title = title, body = body, importance = importance)
        _pendingAlerts.value = listOf(event) + _pendingAlerts.value
            .filter { System.currentTimeMillis() - it.timestamp < ALERT_TTL_MS }
            .take(MAX_ALERTS - 1)
    }

    fun dismissAlert(index: Int) {
        val current = _pendingAlerts.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _pendingAlerts.value = current
        }
    }

    companion object {
        private const val MAX_CAPTIONS = 20
        private const val MAX_ALERTS = 10
        private const val ALERT_TTL_MS = 60_000L
        private const val CAPTION_TTL_MS = 30_000L
    }
}
