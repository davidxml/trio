package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.tts.TtsQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        val description = event.source?.contentDescription?.toString()
            ?: event.source?.text?.toString()
            ?: return

        hapticController.playTick()
        speak(description, flush = true)
    }

    private fun onViewClicked(event: AccessibilityEvent) {
        val description = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: return

        hapticController.playConfirmation()
        speak("$description activated", flush = false)
    }

    private fun speak(text: String, flush: Boolean) {
        scope.launch {
            ttsQueueManager.speak(text, flush)
        }
    }

    companion object {
        private const val TAG = "VisionModeHandler"
    }
}
