package com.trio.service.notification

import android.app.Notification
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.trio.service.camera.FlashAlertController
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.hearing.HearingAlertStateHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioAlertInterceptor @Inject constructor(
    private val flashController: FlashAlertController,
    private val hapticController: HapticFeedbackController,
    private val alertStateHolder: HearingAlertStateHolder
) {

    fun interceptNotification(title: String, body: String, importance: Int) {
        Log.d(TAG, "Intercepting notification (importance=$importance)")

        flashController.flashForNotification(importance)

        if (importance >= Notification.IMPORTANCE_HIGH) {
            hapticController.playUrgentAlert()
        } else {
            hapticController.playNotificationPulse()
        }

        alertStateHolder.pushAlert(title = title, body = body, importance = importance)
    }

    fun shutdown() {
        flashController.shutdown()
    }

    fun interceptAccessibilityEvent(event: AccessibilityEvent) {
        val text = event.source?.text?.toString()
            ?: event.source?.contentDescription?.toString()
            ?: return

        val source = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "System"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "Screen"
            else -> "Event"
        }

        alertStateHolder.pushCaption(text = text, source = source)
    }

    companion object {
        private const val TAG = "AudioAlertInterceptor"
    }
}
