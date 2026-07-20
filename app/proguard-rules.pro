# ── Room ──────────────────────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# ── Hilt ─────────────────────────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keep @dagger.hilt.InstallIn class *
-keepclassmembers class * {
    @javax.inject.* <fields>;
    @javax.inject.* <init>(...);
}
-keep class **_HiltModules* { *; }
-keep class **_HiltComponents* { *; }
-keep class **_GeneratedInjector { *; }
-dontwarn dagger.hilt.internal.**

# ── Hilt Entry Points (critical for Composable DI) ───────────────────────────
-keep @dagger.hilt.EntryPoint class *
-keep @dagger.hilt.EntryPointAccessors class *
-keepclassmembers class * {
    @dagger.hilt.EntryPoint <fields>;
    @dagger.hilt.EntryPoint <methods>;
}

# ── SQLCipher ────────────────────────────────────────────────────────────────
-keep class net.zetetic.** { *; }
-dontwarn net.zetetic.**

# ── Jetpack Compose ──────────────────────────────────────────────────────────
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# ── Security Crypto (EncryptedSharedPreferences) ─────────────────────────────
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# ── Kotlin Serialization ─────────────────────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}
-keepclasseswithmembers class **$$serializer {
    *** INSTANCE;
}

# ── Google Error Prone Annotations (Tink) ───────────────────────────────────
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi

# ── App Models & PackageManager Queries (prevent R8 stripping) ──────────────
-keep class com.trio.presentation.launcher.components.shared.LaunchableApp { *; }
-keep class com.trio.presentation.launcher.components.shared.LaunchableAppKt { *; }
-keep class com.trio.domain.model.** { *; }
-keep class com.trio.service.hearing.CaptionEntry { *; }
-keep class com.trio.service.hearing.AlertEvent { *; }
-keepclassmembers class com.trio.domain.model.** { *; }

# ── Accessibility Service (prevent R8 from stripping event handlers) ─────────
-keep class com.trio.service.accessibility.** { *; }
-keep class com.trio.service.accessibility.config.** { *; }
-keep class com.trio.service.accessibility.handler.** { *; }
-keepclassmembers class com.trio.service.accessibility.** { *; }

# ── Haptic & TTS Services (prevent R8 from stripping injected fields) ────────
-keep class com.trio.service.haptics.** { *; }
-keep class com.trio.service.tts.** { *; }
-keep class com.trio.service.hearing.** { *; }
-keepclassmembers class com.trio.service.haptics.** { *; }
-keepclassmembers class com.trio.service.tts.** { *; }
-keepclassmembers class com.trio.service.hearing.** { *; }

# ── TTS Framework Callbacks (R8 strips lambdas passed to TextToSpeech) ──────
-keep class android.speech.tts.TextToSpeech$OnInitListener { *; }
-keep class android.speech.tts.TextToSpeech$OnUtteranceProgressListener { *; }
-keepclassmembers class android.speech.tts.TextToSpeech {
    public <init>(...);
}
# Preserve the named inner classes that R8 may strip as "unused"
-dontwarn android.speech.tts.**

# ── Icon Loading Pipeline (Drawable → Bitmap → ImageBitmap) ──────────────────
-keep class androidx.core.graphics.drawable.DrawableKt { *; }
-keep class androidx.compose.ui.graphics.ImageBitmapKt { *; }
-keepclassmembers class * extends android.graphics.drawable.Drawable {
    public android.graphics.Bitmap toBitmap(...);
}
-keep class androidx.core.graphics.drawable.** { *; }

# ── Presentation Layer (prevent R8 from stripping click handlers & state) ────
-keep class com.trio.presentation.launcher.** { *; }
-keepclassmembers class com.trio.presentation.launcher.** { *; }

# ── Data Layer (prevent R8 from stripping state holders & repositories) ──────
-keep class com.trio.data.** { *; }
-keepclassmembers class com.trio.data.** { *; }

# ── General ──────────────────────────────────────────────────────────────────
-keepattributes Signature
-keepattributes Exceptions
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
