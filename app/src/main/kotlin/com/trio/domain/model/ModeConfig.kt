package com.trio.domain.model

data class ModeConfig(
    val isTtsEnabled: Boolean = false,
    val isHapticGuidanceEnabled: Boolean = false,
    val isVisualAlertEnabled: Boolean = false,
    val isFlashAlertEnabled: Boolean = false,
    val isLiveCaptionEnabled: Boolean = false
) {
    companion object {
        fun forMode(mode: DeviceMode): ModeConfig = when (mode) {
            DeviceMode.STANDARD -> ModeConfig()
            DeviceMode.VISION_IMPAIRED -> ModeConfig(
                isTtsEnabled = true,
                isHapticGuidanceEnabled = true
            )
            DeviceMode.HEARING_IMPAIRED -> ModeConfig(
                isVisualAlertEnabled = true,
                isFlashAlertEnabled = true,
                isLiveCaptionEnabled = true,
                isHapticGuidanceEnabled = true
            )
            DeviceMode.SPEECH_IMPAIRED -> ModeConfig()
        }
    }
}
