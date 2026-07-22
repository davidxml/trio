package com.trio.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.trio.domain.model.DeviceMode

private val StandardColorScheme = lightColorScheme(
    primary = StandardPrimary,
    onPrimary = StandardOnPrimary,
    background = StandardBackground,
    onBackground = StandardOnBackground,
    surface = StandardSurface,
    onSurface = StandardOnSurface,
    surfaceVariant = StandardSurfaceVariant,
    onSurfaceVariant = StandardOnSurfaceVariant
)

private val HighContrastColorScheme = lightColorScheme(
    primary = HighContrastPrimary,
    onPrimary = HighContrastOnPrimary,
    background = VisionCanvas,
    onBackground = VisionSurfaceBlack,
    surface = HighContrastSurface,
    onSurface = HighContrastOnSurface,
    surfaceVariant = HighContrastSurfaceVariant,
    onSurfaceVariant = HighContrastOnSurfaceVariant
)

private val HearingColorScheme = lightColorScheme(
    primary = HearingPrimary,
    onPrimary = HearingOnPrimary,
    background = HearingBackground,
    onBackground = HearingOnBackground,
    surface = HearingSurface,
    onSurface = HearingOnSurface,
    surfaceVariant = HearingSurfaceVariant,
    onSurfaceVariant = HearingOnSurfaceVariant
)

private val SpeechImpairedColorScheme = lightColorScheme(
    primary = SpeechPrimary,
    onPrimary = SpeechOnPrimary,
    background = SpeechBackground,
    onBackground = SpeechOnBackground,
    surface = SpeechSurface,
    onSurface = SpeechOnSurface,
    surfaceVariant = SpeechSurfaceVariant,
    onSurfaceVariant = SpeechOnSurfaceVariant
)

@Composable
fun ModeThemeProvider(
    mode: DeviceMode,
    textScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = when (mode) {
        DeviceMode.STANDARD -> StandardColorScheme
        DeviceMode.VISION_IMPAIRED -> HighContrastColorScheme
        DeviceMode.HEARING_IMPAIRED -> HearingColorScheme
        DeviceMode.SPEECH_IMPAIRED -> SpeechImpairedColorScheme
    }

    val baseTypography = when (mode) {
        DeviceMode.VISION_IMPAIRED -> LargeScaleTypography
        else -> StandardTypography
    }

    val typography = if (textScale != 1.0f) {
        baseTypography.scale(textScale)
    } else {
        baseTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

private fun TextStyle.scaled(scale: Float): TextStyle = copy(
    fontSize = fontSize * scale,
    lineHeight = lineHeight * scale
)

private fun androidx.compose.material3.Typography.scale(factor: Float): androidx.compose.material3.Typography =
    copy(
        displayLarge = displayLarge.scaled(factor),
        displayMedium = displayMedium.scaled(factor),
        displaySmall = displaySmall.scaled(factor),
        headlineLarge = headlineLarge.scaled(factor),
        headlineMedium = headlineMedium.scaled(factor),
        headlineSmall = headlineSmall.scaled(factor),
        titleLarge = titleLarge.scaled(factor),
        titleMedium = titleMedium.scaled(factor),
        titleSmall = titleSmall.scaled(factor),
        bodyLarge = bodyLarge.scaled(factor),
        bodyMedium = bodyMedium.scaled(factor),
        bodySmall = bodySmall.scaled(factor),
        labelLarge = labelLarge.scaled(factor),
        labelMedium = labelMedium.scaled(factor),
        labelSmall = labelSmall.scaled(factor)
    )
