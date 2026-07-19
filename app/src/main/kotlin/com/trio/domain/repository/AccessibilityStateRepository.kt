package com.trio.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AccessibilityStateRepository {
    val isAccessibilityServiceEnabled: StateFlow<Boolean>
    val isNotificationListenerEnabled: StateFlow<Boolean>
    fun onAccessibilityServiceConnected()
    fun onAccessibilityServiceDisconnected()
    fun onNotificationListenerConnected()
    fun onNotificationListenerDisconnected()
}
