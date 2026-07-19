package com.trio.data.state

import com.trio.data.local.db.UserProfileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileStateHolder @Inject constructor(
    private val profileDao: UserProfileDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _textScale = MutableStateFlow(1.0f)
    val textScale: StateFlow<Float> = _textScale.asStateFlow()

    private val _hapticIntensity = MutableStateFlow(1.0f)
    val hapticIntensity: StateFlow<Float> = _hapticIntensity.asStateFlow()

    init {
        scope.launch {
            profileDao.observeCurrentUser().collect { profile ->
                if (profile != null) {
                    _textScale.value = profile.textScale
                    _hapticIntensity.value = profile.hapticIntensity
                } else {
                    _textScale.value = 1.0f
                    _hapticIntensity.value = 1.0f
                }
            }
        }
    }
}
