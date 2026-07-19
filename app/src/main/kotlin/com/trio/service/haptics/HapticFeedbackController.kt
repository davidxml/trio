package com.trio.service.haptics

import com.trio.data.state.UserProfileStateHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedbackController @Inject constructor(
    private val vibratorWrapper: VibratorCompatWrapper,
    private val userProfileState: UserProfileStateHolder
) {
    fun playTick() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(
                HapticPatternLibrary.NAVIGATION_TICK.scaledAmplitude(userProfileState.hapticIntensity.value)
            )
        }
    }

    fun playBoundaryBuzz() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(
                HapticPatternLibrary.TARGET_BOUNDARY.scaledAmplitude(userProfileState.hapticIntensity.value)
            )
        }
    }

    fun playConfirmation() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(
                HapticPatternLibrary.CONFIRMATION.scaledAmplitude(userProfileState.hapticIntensity.value)
            )
        }
    }

    fun playUrgentAlert() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(
                HapticPatternLibrary.URGENT_ALERT.scaledAmplitude(userProfileState.hapticIntensity.value)
            )
        }
    }

    fun playNotificationPulse() {
        if (vibratorWrapper.hasAmplitudeControl) {
            vibratorWrapper.vibrate(
                HapticPatternLibrary.NOTIFICATION_PULSE.scaledAmplitude(userProfileState.hapticIntensity.value)
            )
        }
    }

    fun cancel() {
        vibratorWrapper.cancel()
    }

    private fun com.trio.domain.model.HapticPattern.scaledAmplitude(intensity: Float): com.trio.domain.model.HapticPattern {
        val clamped = intensity.coerceIn(0f, 1f)
        return copy(
            amplitudes = amplitudes.map { (it * clamped).toInt().coerceIn(0, 255) }.toIntArray()
        )
    }
}
