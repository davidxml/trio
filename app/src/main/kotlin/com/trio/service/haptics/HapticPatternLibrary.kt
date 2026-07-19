package com.trio.service.haptics

import com.trio.domain.model.HapticPattern

object HapticPatternLibrary {

    val NAVIGATION_TICK = HapticPattern(
        durations = longArrayOf(0, 30),
        amplitudes = intArrayOf(0, 128)
    )

    val TARGET_BOUNDARY = HapticPattern(
        durations = longArrayOf(0, 100),
        amplitudes = intArrayOf(0, 255)
    )

    val CONFIRMATION = HapticPattern(
        durations = longArrayOf(0, 50, 50, 50),
        amplitudes = intArrayOf(0, 200, 0, 200)
    )

    val URGENT_ALERT = HapticPattern(
        durations = longArrayOf(0, 80, 60, 80, 60, 80),
        amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
    )

    val NOTIFICATION_PULSE = HapticPattern(
        durations = longArrayOf(0, 120, 80, 120),
        amplitudes = intArrayOf(0, 200, 0, 200)
    )
}
