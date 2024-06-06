package com.snowball.memetory.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private lateinit var context: Context
    private lateinit var encryptedSharedPreferences: SharedPreferences

    fun init(context: Context) {
        this.context = context
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = encryptedSharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? = encryptedSharedPreferences.getString("ACCESS_TOKEN", null)
    fun getRefreshToken(): String? = encryptedSharedPreferences.getString("REFRESH_TOKEN", null)

    fun saveFcmToken(token: String) {
        encryptedSharedPreferences.edit().putString("FCM_TOKEN", token).apply()
    }

    fun setTokenSentToServer(sent: Boolean) {
        encryptedSharedPreferences.edit().putBoolean("TOKEN_SENT", sent).apply()
    }

    fun isTokenSentToServer(): Boolean {
        return encryptedSharedPreferences.getBoolean("TOKEN_SENT", false)
    }

    fun getFCMToken(): String? = encryptedSharedPreferences.getString("FCM_TOKEN", null)

}