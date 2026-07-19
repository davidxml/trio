package com.trio.service.accessibility.config

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.trio.domain.model.DeviceMode

class AccessibilityServiceConfig(private val service: AccessibilityService) {

    fun updateForMode(mode: DeviceMode) {
        val info = service.serviceInfo ?: return

        info.flags = flagsForMode(mode)
        info.eventTypes = eventTypesForMode(mode)

        service.serviceInfo = info
        Log.d(TAG, "Updated service config for $mode")
    }

    private fun flagsForMode(mode: DeviceMode): Int = when (mode) {
        DeviceMode.VISION_IMPAIRED ->
            AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
            AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
        DeviceMode.HEARING_IMPAIRED ->
            AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        DeviceMode.STANDARD, DeviceMode.SPEECH_IMPAIRED ->
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
    }

    private fun eventTypesForMode(mode: DeviceMode): Int = when (mode) {
        DeviceMode.VISION_IMPAIRED ->
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER or
            AccessibilityEvent.TYPE_VIEW_CLICKED
        DeviceMode.HEARING_IMPAIRED ->
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        DeviceMode.STANDARD, DeviceMode.SPEECH_IMPAIRED ->
            AccessibilityEvent.TYPE_ALL_MASK
    }

    companion object {
        private const val TAG = "AccessibilityServiceConfig"
    }
}
