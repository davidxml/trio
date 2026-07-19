package com.trio.service.haptics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedbackController @Inject constructor(
    private val vibratorWrapper: VibratorCompatWrapper
) {
    fun playTick() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(HapticPatternLibrary.NAVIGATION_TICK)
        }
    }

    fun playBoundaryBuzz() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(HapticPatternLibrary.TARGET_BOUNDARY)
        }
    }

    fun playConfirmation() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(HapticPatternLibrary.CONFIRMATION)
        }
    }

    fun playUrgentAlert() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(HapticPatternLibrary.URGENT_ALERT)
        }
    }

    fun playNotificationPulse() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(HapticPatternLibrary.NOTIFICATION_PULSE)
        }
    }

    fun cancel() {
        vibratorWrapper.cancel()
    }
}
