package com.trio.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.trio.core.theme.TrioTheme
import com.trio.core.util.EncryptedPrefsFactory
import com.trio.presentation.launcher.LauncherActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = EncryptedPrefsFactory.create(this, PREFS_NAME)
        if (prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false)) {
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
            return
        }

        setContent {
            TrioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccessibilityPermissionScreen(
                        onCompleted = {
                            prefs.edit()
                                .putBoolean(KEY_ONBOARDING_COMPLETE, true)
                                .apply()

                            startActivity(Intent(this@OnboardingActivity, LauncherActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "trio_onboarding_prefs"
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }
}
