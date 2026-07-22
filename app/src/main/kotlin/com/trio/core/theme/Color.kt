package com.trio.core.theme

import androidx.compose.ui.graphics.Color

// ── Standard Mode (baseline) ──────────────────────────────────────────────
val StandardPrimary = Color(0xFF1A73E8)
val StandardOnPrimary = Color.White
val StandardBackground = Color(0xFFFAFAFA)
val StandardOnBackground = Color(0xFF202124)
val StandardSurface = Color(0xFFFFFFFF)
val StandardOnSurface = Color(0xFF1C1C1E)
val StandardSurfaceVariant = Color(0xFFE8EAED)
val StandardOnSurfaceVariant = Color(0xFF5F6368)
val StandardFab = Color(0xFFB39DDB)          // pastel lavender — unique to Standard now
val StandardSliderActive = Color(0xFF2196F3) // Material Blue 500

// ── Vision Impaired Mode ───────────────────────────────────────────────────
// Rule: yellow NEVER sits directly on the light canvas — only on black surfaces.
val HighContrastPrimary = Color(0xFF0000E0)
val HighContrastOnPrimary = Color(0xFFFFFF00)
val HighContrastBackground = Color.Black
val HighContrastOnBackground = Color(0xFFFFFF00)
val HighContrastSurface = Color(0xFF1A1A1A)
val HighContrastOnSurface = Color(0xFFFFFF00)
val HighContrastSurfaceVariant = Color(0xFF333333)
val HighContrastOnSurfaceVariant = Color(0xFFFFCC00)
val VisionCanvas = Color(0xFFFAFAFA)         // spacing only, never carries text
val VisionSurfaceBlack = Color(0xFF000000)   // header bar + tiles + FAB
val VisionAccentYellow = Color(0xFFFFEB3B)   // ~18:1 contrast on VisionSurfaceBlack
val VisionFab = VisionSurfaceBlack           // was: deep purple — now matches the system
val VisionFabIcon = VisionAccentYellow

// ── Hearing Impaired Mode ──────────────────────────────────────────────────
val HearingPrimary = Color(0xFF00796B)
val HearingOnPrimary = Color.White
val HearingBackground = Color(0xFFFAFAFA)
val HearingOnBackground = Color(0xFF212121)
val HearingSurface = Color.White
val HearingOnSurface = Color(0xFF212121)
val HearingSurfaceVariant = Color(0xFFE0F2F1)
val HearingOnSurfaceVariant = Color(0xFF00695C)
val HearingVisualizerCard = Color(0xFFE0F7FA)     // pale mint surface (kept)
val HearingVisualizerBars = Color(0xFF00695C)     // was faded mint — now ~8:1 contrast
val HearingCaptionBackground = Color(0xFF0D1321)  // deep navy (kept — legitimate pattern)
val HearingCaptionHeader = Color(0xFF26C6DA)      // teal/cyan (kept — already worked)
val HearingCaptionBody = Color(0xFFECEFF1)        // was vague "muted gray" — now ~7.3:1
val HearingAlertCritical = Color(0xFFD32F2F)
val HearingAlertWarning = Color(0xFFFFA000)
val HearingSoundActive = Color(0xFF00E676)
val HearingSoundMuted = Color(0xFF757575)
val HearingFab = Color(0xFF26C6DA)                // was lavender — now ties to mode accent

// ── Speech Impaired Mode ────────────────────────────────────────────────────
val SpeechPrimary = Color(0xFF00897B)
val SpeechOnPrimary = Color.White
val SpeechBackground = Color(0xFFFAFAFA)
val SpeechOnBackground = Color(0xFF212121)
val SpeechSurface = Color.White
val SpeechOnSurface = Color(0xFF212121)
val SpeechSurfaceVariant = Color(0xFFE0F2F1)
val SpeechOnSurfaceVariant = Color(0xFF4DB6AC)
val SpeechPrimaryAction = Color(0xFF00695C)   // dark teal "Speak" button (kept)
val SpeechSecondaryBorder = Color(0xFF00695C) // outline chips/clear button (kept)
val SpeechFab = Color(0xFF00796B)             // was lavender — distinct shade from
                                                //   SpeechPrimaryAction to preserve hierarchy
