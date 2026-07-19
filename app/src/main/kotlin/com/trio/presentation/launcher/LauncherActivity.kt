package com.trio.presentation.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.trio.data.state.UserProfileStateHolder
import com.trio.core.theme.TrioTheme
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.hearing.HearingImpairedHomeScreen
import com.trio.presentation.launcher.components.speech.SpeechImpairedHomeScreen
import com.trio.presentation.launcher.components.standard.StandardHomeScreen
import com.trio.presentation.launcher.components.vision.VisionImpairedHomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    @Inject lateinit var userProfileState: UserProfileStateHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mode by viewModel.mode.collectAsState()

            TrioTheme(mode = mode, userProfileState = userProfileState) {
                when (mode) {
                    DeviceMode.STANDARD -> StandardHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                    DeviceMode.VISION_IMPAIRED -> VisionImpairedHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                    DeviceMode.HEARING_IMPAIRED -> HearingImpairedHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                    DeviceMode.SPEECH_IMPAIRED -> SpeechImpairedHomeScreen(
                        currentMode = mode,
                        onModeSelected = viewModel::setMode
                    )
                }
            }
        }
    }
}
