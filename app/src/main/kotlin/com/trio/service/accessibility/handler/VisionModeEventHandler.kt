package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class VisionModeEventHandler(
    private val service: AccessibilityService
) : ModeEventHandler {

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
        Log.d(TAG, "Hover enter: $description")
    }

    private fun onViewClicked(event: AccessibilityEvent) {
        val description = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: return
        Log.d(TAG, "View clicked: $description")
    }

    companion object {
        private const val TAG = "VisionModeHandler"
    }
}
