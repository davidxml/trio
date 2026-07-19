package com.trio.domain.usecase

import com.trio.domain.model.DeviceMode
import com.trio.domain.model.ModeConfig
import com.trio.domain.repository.ModeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetModeConfigUseCase @Inject constructor(
    private val repository: ModeRepository
) {
    operator fun invoke(): Flow<ModeConfig> = repository.observeMode().map { ModeConfig.forMode(it) }

    operator fun invoke(mode: DeviceMode): ModeConfig = ModeConfig.forMode(mode)
}
