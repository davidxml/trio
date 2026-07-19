package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class HearingModeEventHandler(
    private val service: AccessibilityService
) : ModeEventHandler {

    override fun handleEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> onWindowStateChanged(event)
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> onWindowContentChanged(event)
        }
    }

    private fun onWindowStateChanged(event: AccessibilityEvent) {
        val className = event.className?.toString() ?: return
        Log.d(TAG, "Window state changed: $className")
    }

    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val resourceId = event.source?.viewIdResourceName ?: return
        Log.d(TAG, "Window content changed: $resourceId")
    }

    companion object {
        private const val TAG = "HearingModeHandler"
    }
}
