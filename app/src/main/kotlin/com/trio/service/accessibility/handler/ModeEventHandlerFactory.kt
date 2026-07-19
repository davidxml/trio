package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.domain.model.DeviceMode

interface ModeEventHandler {
    fun handleEvent(event: AccessibilityEvent)
}

class ModeEventHandlerFactory(private val service: AccessibilityService) {

    private val visionHandler by lazy { VisionModeEventHandler(service) }
    private val hearingHandler by lazy { HearingModeEventHandler(service) }

    fun getHandler(mode: DeviceMode): ModeEventHandler = when (mode) {
        DeviceMode.VISION_IMPAIRED -> visionHandler
        DeviceMode.HEARING_IMPAIRED -> hearingHandler
        DeviceMode.STANDARD, DeviceMode.SPEECH_IMPAIRED -> NoOpEventHandler
    }

    private object NoOpEventHandler : ModeEventHandler {
        override fun handleEvent(event: AccessibilityEvent) = Unit
    }
}
