package com.trio.service.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraTorchManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val cameraManager: CameraManager? =
        context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    private val cameraId: String? by lazy {
        try {
            cameraManager?.cameraIdList?.firstOrNull()
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to enumerate cameras", e)
            null
        }
    }

    val isTorchAvailable: Boolean
        get() {
            val id = cameraId ?: return false
            return try {
                val chars = cameraManager?.getCameraCharacteristics(id)
                val available = chars?.get(
                    android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
                ) ?: false
                available == true
            } catch (e: CameraAccessException) {
                Log.e(TAG, "Failed to check torch availability", e)
                false
            }
        }

    suspend fun turnOn() = withContext(Dispatchers.IO) {
        setTorchMode(true)
    }

    suspend fun turnOff() = withContext(Dispatchers.IO) {
        setTorchMode(false)
    }

    private fun setTorchMode(enabled: Boolean) {
        val id = cameraId ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraManager?.setTorchMode(id, enabled)
            } catch (e: CameraAccessException) {
                Log.e(TAG, "Torch mode set failed (enabled=$enabled)", e)
            } catch (e: SecurityException) {
                Log.e(TAG, "Camera permission denied for torch (enabled=$enabled)", e)
            }
        }
    }

    companion object {
        private const val TAG = "CameraTorchManager"
    }
}
