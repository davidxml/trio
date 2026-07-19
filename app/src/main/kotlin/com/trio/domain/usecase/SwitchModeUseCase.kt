package com.trio.domain.usecase

import com.trio.domain.model.DeviceMode
import com.trio.domain.repository.ModeRepository
import javax.inject.Inject

class SwitchModeUseCase @Inject constructor(
    private val repository: ModeRepository
) {
    suspend operator fun invoke(mode: DeviceMode) {
        repository.setMode(mode)
    }
}
