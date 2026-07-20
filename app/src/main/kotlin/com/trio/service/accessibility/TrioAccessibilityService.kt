package com.trio.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.trio.data.state.GlobalModeStateHolder
import com.trio.domain.model.DeviceMode
import com.trio.domain.repository.AccessibilityStateRepository
import com.trio.service.accessibility.config.AccessibilityServiceConfig
import com.trio.service.accessibility.handler.ModeEventHandler
import com.trio.service.accessibility.handler.ModeEventHandlerFactory
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.hearing.HearingAlertStateHolder
import com.trio.service.tts.TtsQueueManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrioAccessibilityService : AccessibilityService() {

    @Inject lateinit var stateHolder: GlobalModeStateHolder
    @Inject lateinit var hapticController: HapticFeedbackController
    @Inject lateinit var ttsQueueManager: TtsQueueManager
    @Inject lateinit var hearingAlertStateHolder: HearingAlertStateHolder
    @Inject lateinit var accessibilityStateRepository: AccessibilityStateRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val handler = Handler(Looper.getMainLooper())
    private var serviceConfig: AccessibilityServiceConfig? = null
    private var handlerFactory: ModeEventHandlerFactory? = null
    private var currentHandler: ModeEventHandler? = null

    private var volumeUpPressed = false
    private var volumeDownPressed = false
    private var escapeHatchRunnable: Runnable? = null

    private val eventTimestamps = HashMap<Int, Long>(64)

    override fun onServiceConnected() {
        super.onServiceConnected()
        accessibilityStateRepository.onAccessibilityServiceConnected()
        serviceConfig = AccessibilityServiceConfig(this)
        handlerFactory = ModeEventHandlerFactory(this, hapticController, ttsQueueManager, hearingAlertStateHolder)

        scope.launch {
            stateHolder.mode.collect { mode ->
                Log.d(TAG, "Mode changed: $mode")
                serviceConfig?.updateForMode(mode)
                currentHandler = handlerFactory?.getHandler(mode)
                Log.d(TAG, "Handler swapped to: ${currentHandler?.javaClass?.simpleName}")
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        val now = System.currentTimeMillis()
        val sourceHash = event.source?.hashCode() ?: 0
        val key = event.eventType xor (sourceHash and 0xFFFF)
        val last = eventTimestamps[key] ?: 0L
        if (now - last < EVENT_THROTTLE_MS) return
        eventTimestamps[key] = now

        if (eventTimestamps.size > 200) {
            eventTimestamps.clear()
        }

        currentHandler?.handleEvent(event)
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                volumeUpPressed = event.action == KeyEvent.ACTION_DOWN
                checkEscapeHatch()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                volumeDownPressed = event.action == KeyEvent.ACTION_DOWN
                checkEscapeHatch()
                return true
            }
        }
        return super.onKeyEvent(event)
    }

    private fun checkEscapeHatch() {
        escapeHatchRunnable?.let { handler.removeCallbacks(it) }
        escapeHatchRunnable = null

        if (volumeUpPressed && volumeDownPressed) {
            Log.d(TAG, "Escape hatch: both volume buttons pressed, starting 5s timer")
            escapeHatchRunnable = Runnable {
                if (volumeUpPressed && volumeDownPressed) {
                    Log.w(TAG, "ESCAPE HATCH TRIGGERED — forcing STANDARD mode")
                    hapticController.playUrgentAlert()
                    scope.launch {
                        stateHolder.setMode(DeviceMode.STANDARD)
                    }
                }
            }
            handler.postDelayed(escapeHatchRunnable!!, ESCAPE_HATCH_DELAY_MS)
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }

    override fun onDestroy() {
        escapeHatchRunnable?.let { handler.removeCallbacks(it) }
        handlerFactory?.destroy()
        ttsQueueManager.shutdown()
        accessibilityStateRepository.onAccessibilityServiceDisconnected()
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TrioAccessibilityService"
        private const val ESCAPE_HATCH_DELAY_MS = 5000L
        private const val EVENT_THROTTLE_MS = 150L
    }
}
