package com.trio.data.repository

import com.trio.domain.repository.AccessibilityStateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityStateRepositoryImpl @Inject constructor() : AccessibilityStateRepository {

    private val _isAccessibilityServiceEnabled = MutableStateFlow(false)
    override val isAccessibilityServiceEnabled: StateFlow<Boolean> =
        _isAccessibilityServiceEnabled.asStateFlow()

    private val _isNotificationListenerEnabled = MutableStateFlow(false)
    override val isNotificationListenerEnabled: StateFlow<Boolean> =
        _isNotificationListenerEnabled.asStateFlow()

    override fun onAccessibilityServiceConnected() {
        _isAccessibilityServiceEnabled.value = true
    }

    override fun onAccessibilityServiceDisconnected() {
        _isAccessibilityServiceEnabled.value = false
    }

    override fun onNotificationListenerConnected() {
        _isNotificationListenerEnabled.value = true
    }

    override fun onNotificationListenerDisconnected() {
        _isNotificationListenerEnabled.value = false
    }
}
