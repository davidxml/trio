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
}
