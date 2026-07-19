package com.trio.data.local.db

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.trio.core.util.EncryptedPrefsFactory
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.KeyGenerator

object DatabaseKeyManager {

    private const val PREFS_FILE_NAME = "trio_db_key_prefs"
    private const val KEY_DB_PASSPHRASE = "db_passphrase"
    private const val HARDWARE_CHECK_ALIAS = "_trio_hw_check_"

    fun getPassphrase(context: Context): ByteArray {
        requireHardwareBackedKeystore()

        val encryptedPrefs = EncryptedPrefsFactory.create(context, PREFS_FILE_NAME)

        val existing = encryptedPrefs.getString(KEY_DB_PASSPHRASE, null)
        if (existing != null) {
            return Base64.decode(existing, Base64.NO_WRAP)
        }

        val key = ByteArray(32)
        SecureRandom().nextBytes(key)
        val encoded = Base64.encodeToString(key, Base64.NO_WRAP)
        val persisted = encryptedPrefs.edit().putString(KEY_DB_PASSPHRASE, encoded).commit()
        if (!persisted) {
            key.fill(0)
            throw SecurityException("Failed to persist database passphrase to encrypted storage")
        }
        return key
    }

    private fun requireHardwareBackedKeystore() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            throw SecurityException(
                "Hardware-backed Keystore requires API 28+. " +
                    "Current device API: ${Build.VERSION.SDK_INT}"
            )
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        val spec = KeyGenParameterSpec.Builder(
            HARDWARE_CHECK_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        val testKey = keyGenerator.generateKey()

        val isHardwareBacked = testKey.javaClass
            .getMethod("isInsideSecureHardware")
            .invoke(testKey) as Boolean

        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        keyStore.deleteEntry(HARDWARE_CHECK_ALIAS)

        if (!isHardwareBacked) {
            throw SecurityException(
                "Hardware-backed Keystore is required but unavailable on this device. " +
                    "Trio cannot securely store database encryption keys without TEE/StrongBox."
            )
        }
    }
}
