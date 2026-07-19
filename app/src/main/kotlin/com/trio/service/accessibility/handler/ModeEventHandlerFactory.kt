package com.trio.service.accessibility.handler

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.trio.domain.model.DeviceMode
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.hearing.HearingAlertStateHolder
import com.trio.service.tts.TtsQueueManager

interface ModeEventHandler {
    fun handleEvent(event: AccessibilityEvent)
}

class ModeEventHandlerFactory(
    private val service: AccessibilityService,
    private val hapticController: HapticFeedbackController,
    private val ttsQueueManager: TtsQueueManager,
    private val hearingAlertStateHolder: HearingAlertStateHolder
) {

    private val visionHandler by lazy {
        VisionModeEventHandler(service, hapticController, ttsQueueManager)
    }
    private val hearingHandler by lazy {
        HearingModeEventHandler(service, hearingAlertStateHolder)
    }
    private val speechHandler by lazy {
        SpeechModeEventHandler(service, hapticController, ttsQueueManager)
    }

    fun getHandler(mode: DeviceMode): ModeEventHandler = when (mode) {
        DeviceMode.VISION_IMPAIRED -> visionHandler
        DeviceMode.HEARING_IMPAIRED -> hearingHandler
        DeviceMode.SPEECH_IMPAIRED -> speechHandler
        DeviceMode.STANDARD -> NoOpEventHandler
    }

    fun destroy() {
        visionHandler.destroy()
        speechHandler.destroy()
    }

    private object NoOpEventHandler : ModeEventHandler {
        override fun handleEvent(event: AccessibilityEvent) = Unit
    }
}
