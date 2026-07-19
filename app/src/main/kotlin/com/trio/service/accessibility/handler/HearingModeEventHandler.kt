package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.service.hearing.HearingAlertStateHolder

class HearingModeEventHandler(
    private val service: AccessibilityService,
    private val alertStateHolder: HearingAlertStateHolder
) : ModeEventHandler {

    override fun handleEvent(event: AccessibilityEvent) {
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

        alertStateHolder.pushCaption(text = text, source = "System")
    }

    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val text = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: return

        alertStateHolder.pushCaption(text = text, source = "Screen")
    }
}
