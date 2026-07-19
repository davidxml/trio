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
import com.trio.presentation.launcher.LauncherActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("trio_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("onboarding_complete", false)) {
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
                                .putBoolean("onboarding_complete", true)
                                .apply()

                            startActivity(Intent(this@OnboardingActivity, LauncherActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}
