package com.trio.service.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.trio.data.state.GlobalModeStateHolder
import com.trio.domain.model.DeviceMode
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

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var currentMode: DeviceMode = DeviceMode.STANDARD

    override fun onCreate() {
        super.onCreate()
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
        // No-op for now
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TrioNotificationListener"
    }
}
