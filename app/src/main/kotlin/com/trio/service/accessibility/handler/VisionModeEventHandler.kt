package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.tts.TtsQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class VisionModeEventHandler(
    private val service: AccessibilityService,
    private val hapticController: HapticFeedbackController,
    private val ttsQueueManager: TtsQueueManager
) : ModeEventHandler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun handleEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> onHoverEnter(event)
            AccessibilityEvent.TYPE_VIEW_CLICKED -> onViewClicked(event)
        }
    }

    private fun onHoverEnter(event: AccessibilityEvent) {
        val nodeInfo = event.source ?: return
        if (nodeInfo.isPassword) return

        val description = nodeInfo.contentDescription?.toString()
            ?: nodeInfo.text?.toString()
            ?: return

        hapticController.playTick()
        speak(description, flush = true)
    }

    private fun onViewClicked(event: AccessibilityEvent) {
        val nodeInfo = event.source ?: return
        if (nodeInfo.isPassword) return

        val description = nodeInfo.text?.toString()
            ?: nodeInfo.contentDescription?.toString()
            ?: return

        hapticController.playConfirmation()
        speak("$description activated", flush = false)
    }

    private fun speak(text: String, flush: Boolean) {
        scope.launch {
            ttsQueueManager.speak(text, flush)
        }
    }

    fun destroy() {
        scope.cancel()
    }

    companion object {
        private const val TAG = "VisionModeHandler"
    }
}
