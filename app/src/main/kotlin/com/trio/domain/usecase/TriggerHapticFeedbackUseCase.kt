package com.trio.domain.usecase

import com.trio.service.haptics.HapticFeedbackController
import javax.inject.Inject

class TriggerHapticFeedbackUseCase @Inject constructor(
    private val hapticController: HapticFeedbackController
) {
    fun playTick() = hapticController.playTick()

    fun playBoundaryBuzz() = hapticController.playBoundaryBuzz()

    fun playConfirmation() = hapticController.playConfirmation()

    fun playUrgentAlert() = hapticController.playUrgentAlert()

    fun playNotificationPulse() = hapticController.playNotificationPulse()

    fun cancel() = hapticController.cancel()
}
