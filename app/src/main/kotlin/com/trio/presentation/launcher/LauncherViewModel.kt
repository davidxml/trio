package com.trio.presentation.launcher

import androidx.lifecycle.ViewModel
import com.trio.data.state.GlobalModeStateHolder
import com.trio.domain.model.DeviceMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val stateHolder: GlobalModeStateHolder
) : ViewModel() {

    val mode: StateFlow<DeviceMode> = stateHolder.mode

    fun setMode(m: DeviceMode) {
        stateHolder.setMode(m)
    }
}
