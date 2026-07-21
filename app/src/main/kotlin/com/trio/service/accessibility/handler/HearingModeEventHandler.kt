package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.trio.service.hearing.HearingAlertStateHolder

class HearingModeEventHandler(
    private val service: AccessibilityService,
    private val alertStateHolder: HearingAlertStateHolder
) : ModeEventHandler {

    private var lastCaptionTime = 0L
    private var lastCaptionHash = 0
    private var suppressUntil = 0L
    private val selfPackageName: String = service.packageName

    @Volatile
    private var isProcessing = false

    override fun handleEvent(event: AccessibilityEvent) {
        if (isProcessing) return

        val now = System.currentTimeMillis()
        if (now < suppressUntil) return
        if (now - lastCaptionTime < CAPTION_THROTTLE_MS) return

        val eventPackage = event.packageName?.toString()
        if (eventPackage == selfPackageName) return

        isProcessing = true
        try {
            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> onWindowStateChanged(event)
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> onWindowContentChanged(event)
            }
        } finally {
            isProcessing = false
        }
    }

    private fun onWindowStateChanged(event: AccessibilityEvent) {
        val text = extractText(event) ?: return
        val hash = text.hashCode()
        if (hash == lastCaptionHash) return

        lastCaptionHash = hash
        lastCaptionTime = System.currentTimeMillis()
        suppressUntil = lastCaptionTime + CAPTION_SUPPRESS_AFTER_PUSH_MS

        alertStateHolder.pushCaption(text = text, source = SOURCE_SYSTEM)
    }

    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val text = extractText(event) ?: return
        val hash = text.hashCode()
        if (hash == lastCaptionHash) return

        lastCaptionHash = hash
        lastCaptionTime = System.currentTimeMillis()
        suppressUntil = lastCaptionTime + CAPTION_SUPPRESS_AFTER_PUSH_MS

        alertStateHolder.pushCaption(text = text, source = SOURCE_SCREEN)
    }

    private fun extractText(event: AccessibilityEvent): String? {
        val source = event.source ?: return null
        val text = source.text?.toString()
        if (!text.isNullOrBlank()) {
            return text
        }
        val desc = source.contentDescription?.toString()
        if (!desc.isNullOrBlank()) {
            return desc
        }
        val className = event.className?.toString()
        if (!className.isNullOrBlank()) {
            return className
        }
        return null
    }

    companion object {
        private const val TAG = "HearingModeHandler"
        private const val CAPTION_THROTTLE_MS = 500L
        private const val CAPTION_SUPPRESS_AFTER_PUSH_MS = 1000L
        private const val SOURCE_SYSTEM = "System"
        private const val SOURCE_SCREEN = "Screen"
    }
}
