package com.trio.service.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.trio.data.state.GlobalModeStateHolder
import com.trio.domain.model.DeviceMode
import com.trio.domain.repository.AccessibilityStateRepository
import com.trio.service.hearing.HearingAlertStateHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrioNotificationListenerService : NotificationListenerService() {

    @Inject lateinit var stateHolder: GlobalModeStateHolder
    @Inject lateinit var interceptor: AudioAlertInterceptor
    @Inject lateinit var accessibilityStateRepository: AccessibilityStateRepository
    @Inject lateinit var alertStateHolder: HearingAlertStateHolder

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var currentMode: DeviceMode = DeviceMode.STANDARD

    override fun onCreate() {
        super.onCreate()
        accessibilityStateRepository.onNotificationListenerConnected()
        scope.launch {
            stateHolder.mode.collect { mode ->
                currentMode = mode
                Log.d(TAG, "Mode updated: $mode")
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        if (currentMode != DeviceMode.HEARING_IMPAIRED) return

        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: return
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val importance = notification.priority

        interceptor.interceptNotification(title = title, body = text, importance = importance)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn ?: return
        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: return
        alertStateHolder.dismissAlertByTitle(title)
    }

    override fun onDestroy() {
        interceptor.shutdown()
        accessibilityStateRepository.onNotificationListenerDisconnected()
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TrioNotificationListener"
    }
}
