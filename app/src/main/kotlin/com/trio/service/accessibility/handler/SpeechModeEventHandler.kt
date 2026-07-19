package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.tts.TtsQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class SpeechModeEventHandler(
    private val service: AccessibilityService,
    private val hapticController: HapticFeedbackController,
    private val ttsQueueManager: TtsQueueManager
) : ModeEventHandler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun handleEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> onTextChanged(event)
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> onViewFocused(event)
        }
    }

    private fun onTextChanged(event: AccessibilityEvent) {
        val nodeInfo = event.source ?: return
        if (nodeInfo.isPassword) return
        hapticController.playTick()
    }

    private fun onViewFocused(event: AccessibilityEvent) {
        val nodeInfo = event.source ?: return
        if (nodeInfo.isPassword) return
        hapticController.playTick()
    }

    fun destroy() {
        scope.cancel()
    }

    companion object {
        private const val TAG = "SpeechModeHandler"
    }
}
