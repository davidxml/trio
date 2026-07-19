package com.trio.domain.usecase

import com.trio.domain.model.DeviceMode
import com.trio.domain.repository.ModeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentModeUseCase @Inject constructor(
    private val repository: ModeRepository
) {
    operator fun invoke(): Flow<DeviceMode> {
        return repository.observeMode()
    }
}
