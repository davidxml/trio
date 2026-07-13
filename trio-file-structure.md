# Trio — File Structure (Clean Architecture, single Gradle module)

Rationale: launcher (HOME) + AccessibilityService are two entry points into the *same* state.
Everything routes through one `StateFlow<DeviceMode>` in `data/state`, so UI and OS-layer hooks
never drift out of sync. Domain layer has zero Android/Compose imports — testable in pure JVM.

```
trio/
├── app/
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   └── kotlin/com/trio/
│   │       ├── TrioApplication.kt
│   │       │
│   │       ├── core/
│   │       │   ├── di/
│   │       │   │   ├── AppModule.kt
│   │       │   │   ├── ServiceModule.kt
│   │       │   │   └── RepositoryModule.kt
│   │       │   ├── util/
│   │       │   │   ├── Constants.kt
│   │       │   │   └── Extensions.kt
│   │       │   └── theme/
│   │       │       ├── Color.kt
│   │       │       ├── Typography.kt
│   │       │       ├── Theme.kt
│   │       │       └── ModeThemeProvider.kt        # maps DeviceMode -> contrast/scale tokens
│   │       │
│   │       ├── domain/                              # pure Kotlin, no Android deps
│   │       │   ├── model/
│   │       │   │   ├── DeviceMode.kt                # sealed class: Standard/VisionImpaired/
│   │       │   │   │                                #   HearingImpaired/SpeechImpaired
│   │       │   │   ├── ModeConfig.kt                # per-mode capability flags
│   │       │   │   └── HapticPattern.kt
│   │       │   ├── repository/                      # interfaces only
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
│   │       │   │   │   ├── ModePreferencesDataStore.kt   # persists last-active mode
│   │       │   │   │   └── ModeSerializer.kt
│   │       │   │   └── db/                               # only if multi-user shared-device profiles
│   │       │   │       ├── TrioDatabase.kt
│   │       │   │       ├── UserProfileDao.kt
│   │       │   │       └── UserProfileEntity.kt
│   │       │   └── state/
│   │       │       └── GlobalModeStateHolder.kt     # single source of truth StateFlow<DeviceMode>
│   │       │                                          # both LauncherViewModel and the
│   │       │                                          # AccessibilityService read/write this
│   │       │
│   │       ├── presentation/
│   │       │   ├── launcher/
│   │       │   │   ├── LauncherActivity.kt          # declares HOME intent-filter
│   │       │   │   ├── LauncherViewModel.kt
│   │       │   │   ├── LauncherUiState.kt
│   │       │   │   └── components/
│   │       │   │       ├── standard/
│   │       │   │       │   ├── StandardHomeScreen.kt
│   │       │   │       │   └── StandardAppGrid.kt
│   │       │   │       ├── vision/
│   │       │   │       │   ├── VisionImpairedHomeScreen.kt
│   │       │   │       │   ├── HighContrastTouchZone.kt
│   │       │   │       │   └── TtsAnnouncer.kt        # Compose-side semantics -> TTS hook
│   │       │   │       ├── hearing/
│   │       │   │       │   ├── HearingImpairedHomeScreen.kt
│   │       │   │       │   ├── LiveCaptionOverlay.kt
│   │       │   │       │   └── VisualAlertBanner.kt
│   │       │   │       └── shared/
│   │       │   │           ├── ModeSwitcherFab.kt
│   │       │   │           └── ModeSwitcherDialog.kt
│   │       │   ├── settings/
│   │       │   │   ├── SettingsActivity.kt
│   │       │   │   ├── SettingsViewModel.kt
│   │       │   │   └── ProfileManagementScreen.kt   # for shared-device multi-profile use case
│   │       │   └── onboarding/
│   │       │       ├── OnboardingActivity.kt
│   │       │       └── AccessibilityPermissionScreen.kt  # walks user through granting the service
│   │       │
│   │       └── service/
│   │           ├── accessibility/
│   │           │   ├── TrioAccessibilityService.kt
│   │           │   ├── handler/
│   │           │   │   ├── ModeEventHandlerFactory.kt
│   │           │   │   ├── VisionModeEventHandler.kt    # touch exploration + TTS dispatch
│   │           │   │   └── HearingModeEventHandler.kt   # notification/audio interception
│   │           │   └── config/
│   │           │       └── AccessibilityServiceConfig.kt # dynamic capability toggles per mode
│   │           ├── tts/
│   │           │   ├── TrioTtsEngine.kt
│   │           │   └── TtsQueueManager.kt
│   │           ├── haptics/
│   │           │   ├── HapticFeedbackController.kt
│   │           │   ├── HapticPatternLibrary.kt          # guidance pulses, alert patterns
│   │           │   └── VibratorCompatWrapper.kt         # API-level vibrator abstraction
│   │           ├── camera/
│   │           │   ├── CameraTorchManager.kt
│   │           │   └── FlashAlertController.kt          # flash-on-ring for HearingImpaired mode
│   │           └── notification/
│   │               ├── TrioNotificationListenerService.kt
│   │               └── AudioAlertInterceptor.kt
│   │
│   └── src/main/res/
│       ├── xml/
│       │   └── accessibility_service_config.xml
│       └── values/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md

