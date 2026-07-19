# Trio — File Structure (Clean Architecture, single Gradle module)

Rationale: launcher (HOME) + AccessibilityService are two entry points into the *same* state.
Everything routes through one `StateFlow<DeviceMode>` in `data/state`, so UI and OS-layer hooks
never drift out of sync. Domain layer has zero Android/Compose imports — testable in pure JVM.

```
trio/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   └── kotlin/com/trio/
│   │       ├── TrioApplication.kt
│   │       │
│   │       ├── core/
│   │       │   ├── di/
│   │       │   │   ├── AppModule.kt
│   │       │   │   └── RepositoryModule.kt
│   │       │   ├── util/
│   │       │   │   ├── Constants.kt
│   │       │   │   └── EncryptedPrefsFactory.kt
│   │       │   └── theme/
│   │       │       ├── Color.kt
│   │       │       ├── Typography.kt
│   │       │       ├── Theme.kt
│   │       │       └── ModeThemeProvider.kt
│   │       │
│   │       ├── domain/
│   │       │   ├── model/
│   │       │   │   ├── DeviceMode.kt
│   │       │   │   ├── ModeConfig.kt
│   │       │   │   └── HapticPattern.kt
│   │       │   ├── repository/
│   │       │   │   ├── ModeRepository.kt
│   │       │   │   └── AccessibilityStateRepository.kt
│   │       │   └── usecase/
│   │       │       ├── SwitchModeUseCase.kt
│   │       │       ├── ObserveCurrentModeUseCase.kt
│   │       │       ├── GetModeConfigUseCase.kt
│   │       │       └── TriggerHapticFeedbackUseCase.kt
│   │       │
│   │       ├── data/
│   │       │   ├── repository/
│   │       │   │   ├── ModeRepositoryImpl.kt
│   │       │   │   └── AccessibilityStateRepositoryImpl.kt
│   │       │   ├── local/
│   │       │   │   ├── datastore/
│   │       │   │   │   ├── ModePreferencesDataStore.kt
│   │       │   │   │   └── ModeSerializer.kt
│   │       │   │   └── db/
│   │       │   │       ├── TrioDatabase.kt
│   │       │   │       ├── DatabaseKeyManager.kt
│   │       │   │       ├── UserProfileDao.kt
│   │       │   │       └── UserProfileEntity.kt
│   │       │   └── state/
│   │       │       ├── GlobalModeStateHolder.kt
│   │       │       └── UserProfileStateHolder.kt
│   │       │
│   │       ├── presentation/
│   │       │   ├── launcher/
│   │       │   │   ├── LauncherActivity.kt
│   │       │   │   ├── LauncherViewModel.kt
│   │       │   │   ├── LauncherUiState.kt
│   │       │   │   └── components/
│   │       │   │       ├── standard/
│   │       │   │       │   ├── StandardHomeScreen.kt
│   │       │   │       │   └── StandardAppGrid.kt
│   │       │   │       ├── vision/
│   │       │   │       │   ├── VisionImpairedHomeScreen.kt
│   │       │   │       │   ├── HighContrastTouchZone.kt
│   │       │   │       │   └── TtsAnnouncer.kt
│   │       │   │       ├── hearing/
│   │       │   │       │   ├── HearingImpairedHomeScreen.kt
│   │       │   │       │   ├── HearingTouchZone.kt
│   │       │   │       │   ├── LiveCaptionOverlay.kt
│   │       │   │       │   └── VisualAlertBanner.kt
│   │       │   │       ├── speech/
│   │       │   │       │   ├── SpeechImpairedHomeScreen.kt
│   │       │   │       │   ├── SpeechTouchZone.kt
│   │       │   │       │   └── AacChip.kt
│   │       │   │       └── shared/
│   │       │   │           ├── ModeSwitcherFab.kt
│   │       │   │           ├── ModeSwitcherDialog.kt
│   │       │   │           └── LaunchableApp.kt
│   │       │   ├── settings/
│   │       │   │   ├── SettingsActivity.kt
│   │       │   │   ├── SettingsViewModel.kt
│   │       │   │   └── ProfileManagementScreen.kt
│   │       │   └── onboarding/
│   │       │       ├── OnboardingActivity.kt
│   │       │       └── AccessibilityPermissionScreen.kt
│   │       │
│   │       └── service/
│   │           ├── accessibility/
│   │           │   ├── TrioAccessibilityService.kt
│   │           │   ├── handler/
│   │           │   │   ├── ModeEventHandlerFactory.kt
│   │           │   │   ├── VisionModeEventHandler.kt
│   │           │   │   ├── HearingModeEventHandler.kt
│   │           │   │   └── SpeechModeEventHandler.kt
│   │           │   └── config/
│   │           │       └── AccessibilityServiceConfig.kt
│   │           ├── tts/
│   │           │   ├── TrioTtsEngine.kt
│   │           │   └── TtsQueueManager.kt
│   │           ├── haptics/
│   │           │   ├── HapticFeedbackController.kt
│   │           │   ├── HapticPatternLibrary.kt
│   │           │   └── VibratorCompatWrapper.kt
│   │           ├── camera/
│   │           │   ├── CameraTorchManager.kt
│   │           │   └── FlashAlertController.kt
│   │           ├── hearing/
│   │           │   └── HearingAlertStateHolder.kt
│   │           └── notification/
│   │               ├── TrioNotificationListenerService.kt
│   │               └── AudioAlertInterceptor.kt
│   │
│   ├── src/main/res/
│   │   ├── xml/
│   │   │   └── accessibility_service_config.xml
│   │   └── values/
│   │       └── strings.xml
│   │
│   └── src/test/kotlin/com/trio/
│       ├── domain/model/
│       │   └── ModeConfigTest.kt
│       ├── data/state/
│       │   └── GlobalModeStateHolderTest.kt
│       └── service/hearing/
│           └── HearingAlertStateHolderTest.kt
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Sprint 3 additions
- `speech/` — Speech Impaired mode UI (SpeechImpairedHomeScreen, SpeechTouchZone, AacChip)
- `SpeechModeEventHandler.kt` — Accessibility event handler for speech input mode
- `EncryptedPrefsFactory.kt` — Shared encrypted SharedPreferences factory
- `HearingAlertStateHolder.kt` — Combined caption + alert state for hearing mode
- `LaunchableApp.kt` — Shared utility for querying installed launchable apps

## Sprint 5 additions
- `proguard-rules.pro` — R8 keep rules for Room, Hilt, SQLCipher, Compose, Security Crypto, Serialization
- Test infrastructure: `src/test/` with ModeConfigTest, GlobalModeStateHolderTest, HearingAlertStateHolderTest
