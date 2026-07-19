package com.trio.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trio.data.local.db.UserProfileDao
import com.trio.data.local.db.UserProfileEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileDao: UserProfileDao
) : ViewModel() {

    val profiles = profileDao.observeProfiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val currentUser = profileDao.observeCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun addProfile(name: String) {
        viewModelScope.launch {
            val profile = UserProfileEntity(name = name)
            profileDao.insert(profile)
        }
    }

    fun switchProfile(id: Long) {
        viewModelScope.launch {
            profileDao.clearCurrentUser()
            profileDao.setCurrentUser(id)
        }
    }

    fun updateTextScale(id: Long, scale: Float) {
        viewModelScope.launch {
            profileDao.getById(id)?.let { profile ->
                profileDao.update(profile.copy(textScale = scale))
            }
        }
    }

    fun updateHapticIntensity(id: Long, intensity: Float) {
        viewModelScope.launch {
            profileDao.getById(id)?.let { profile ->
                profileDao.update(profile.copy(hapticIntensity = intensity))
            }
        }
    }

    fun deleteProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            profileDao.delete(profile)
        }
    }
}
