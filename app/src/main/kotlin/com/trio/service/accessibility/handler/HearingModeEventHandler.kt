package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.service.hearing.HearingAlertStateHolder

class HearingModeEventHandler(
    private val service: AccessibilityService,
    private val alertStateHolder: HearingAlertStateHolder
) : ModeEventHandler {

    private var lastCaptionTime = 0L
    private var lastCaptionText = ""

    override fun handleEvent(event: AccessibilityEvent) {
        val now = System.currentTimeMillis()
        if (now - lastCaptionTime < CAPTION_THROTTLE_MS) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> onWindowStateChanged(event)
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> onWindowContentChanged(event)
        }
    }

    private fun onWindowStateChanged(event: AccessibilityEvent) {
        val text = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: event.className?.toString()
            ?: return

        if (text == lastCaptionText) return
        lastCaptionText = text
        lastCaptionTime = System.currentTimeMillis()
        alertStateHolder.pushCaption(text = text, source = "System")
    }

    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val text = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: return

        if (text == lastCaptionText) return
        lastCaptionText = text
        lastCaptionTime = System.currentTimeMillis()
        alertStateHolder.pushCaption(text = text, source = "Screen")
    }

    companion object {
        private const val CAPTION_THROTTLE_MS = 300L
    }
}
