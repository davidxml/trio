package com.trio.presentation.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.trio.core.theme.TrioTheme
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.hearing.HearingImpairedHomeScreen
import com.trio.presentation.launcher.components.standard.StandardHomeScreen
import com.trio.presentation.launcher.components.vision.VisionImpairedHomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mode by viewModel.mode.collectAsState()

            TrioTheme(mode = mode) {
                when (mode) {
                    DeviceMode.Standard -> StandardHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                    DeviceMode.VisionImpaired -> VisionImpairedHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                    DeviceMode.Deaf -> HearingImpairedHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                }
            }
        }
    }
}
