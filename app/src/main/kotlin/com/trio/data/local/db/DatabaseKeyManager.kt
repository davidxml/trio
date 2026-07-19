package com.trio.data.local.db

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

object DatabaseKeyManager {

    private const val PREFS_FILE_NAME = "trio_db_key_prefs"
    private const val KEY_DB_PASSPHRASE = "db_passphrase"

    fun getPassphrase(context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val existing = encryptedPrefs.getString(KEY_DB_PASSPHRASE, null)
        if (existing != null) {
            return Base64.decode(existing, Base64.NO_WRAP)
        }

        val key = ByteArray(32)
        SecureRandom().nextBytes(key)
        val encoded = Base64.encodeToString(key, Base64.NO_WRAP)
        encryptedPrefs.edit().putString(KEY_DB_PASSPHRASE, encoded).apply()
        return key
    }
}
