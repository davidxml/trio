package com.trio.data.repository

import com.trio.data.local.datastore.ModePreferencesDataStore
import com.trio.domain.model.DeviceMode
import com.trio.domain.repository.ModeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModeRepositoryImpl @Inject constructor(
    private val dataStore: ModePreferencesDataStore
) : ModeRepository {

    override fun observeMode(): Flow<DeviceMode> = dataStore.modeFlow

    override suspend fun setMode(mode: DeviceMode) {
        dataStore.setMode(mode)
    }
}
