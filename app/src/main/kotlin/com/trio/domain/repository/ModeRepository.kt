package com.trio.domain.repository

import com.trio.domain.model.DeviceMode
import kotlinx.coroutines.flow.Flow

interface ModeRepository {
    fun observeMode(): Flow<DeviceMode>
    suspend fun setMode(mode: DeviceMode)
}
