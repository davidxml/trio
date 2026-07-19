package com.trio.presentation.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trio.domain.model.DeviceMode
import com.trio.domain.usecase.ObserveCurrentModeUseCase
import com.trio.domain.usecase.SwitchModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    observeCurrentModeUseCase: ObserveCurrentModeUseCase,
    private val switchModeUseCase: SwitchModeUseCase
) : ViewModel() {

    val mode: StateFlow<DeviceMode> = observeCurrentModeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DeviceMode.STANDARD
        )

    fun setMode(m: DeviceMode) {
        viewModelScope.launch {
            switchModeUseCase(m)
        }
    }
}
