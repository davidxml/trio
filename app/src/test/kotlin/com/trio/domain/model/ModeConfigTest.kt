package com.trio.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModeConfigTest {

    @Test
    fun `STANDARD mode has all features disabled`() {
        val config = ModeConfig.forMode(DeviceMode.STANDARD)
        assertFalse(config.isTtsEnabled)
        assertFalse(config.isHapticGuidanceEnabled)
        assertFalse(config.isVisualAlertEnabled)
        assertFalse(config.isFlashAlertEnabled)
        assertFalse(config.isLiveCaptionEnabled)
        assertFalse(config.isSpeechToTextEnabled)
    }

    @Test
    fun `VISION_IMPAIRED mode enables TTS and haptic guidance`() {
        val config = ModeConfig.forMode(DeviceMode.VISION_IMPAIRED)
        assertTrue(config.isTtsEnabled)
        assertTrue(config.isHapticGuidanceEnabled)
        assertFalse(config.isVisualAlertEnabled)
        assertFalse(config.isFlashAlertEnabled)
        assertFalse(config.isLiveCaptionEnabled)
        assertFalse(config.isSpeechToTextEnabled)
    }

    @Test
    fun `HEARING_IMPAIRED mode enables visual alerts, flash, captions, and haptics`() {
        val config = ModeConfig.forMode(DeviceMode.HEARING_IMPAIRED)
        assertFalse(config.isTtsEnabled)
        assertTrue(config.isHapticGuidanceEnabled)
        assertTrue(config.isVisualAlertEnabled)
        assertTrue(config.isFlashAlertEnabled)
        assertTrue(config.isLiveCaptionEnabled)
        assertFalse(config.isSpeechToTextEnabled)
    }

    @Test
    fun `SPEECH_IMPAIRED mode enables speech-to-text and haptic guidance`() {
        val config = ModeConfig.forMode(DeviceMode.SPEECH_IMPAIRED)
        assertFalse(config.isTtsEnabled)
        assertTrue(config.isHapticGuidanceEnabled)
        assertFalse(config.isVisualAlertEnabled)
        assertFalse(config.isFlashAlertEnabled)
        assertFalse(config.isLiveCaptionEnabled)
        assertTrue(config.isSpeechToTextEnabled)
    }

    @Test
    fun `all four DeviceMode values are covered`() {
        val modes = DeviceMode.entries
        assertEquals(4, modes.size)
        modes.forEach { mode ->
            val config = ModeConfig.forMode(mode)
            assertNotNull("ModeConfig for $mode should not be null", config)
        }
    }

    private fun assertNotNull(message: String, value: Any?) {
        org.junit.Assert.assertNotNull(message, value)
    }
}
