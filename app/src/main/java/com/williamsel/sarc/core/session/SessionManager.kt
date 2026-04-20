package com.williamsel.sarc.core.session

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("sarc_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, rol: String, idUsuario: Int) {
        prefs.edit()
            .putString("jwt", token)
            .putString("rol", rol)
            .putInt("id_usuario", idUsuario)
            .apply()
    }

    fun getToken(): String? = prefs.getString("jwt", null)
    fun getRol(): String?   = prefs.getString("rol", null)
    fun getUserId(): Int    = prefs.getInt("id_usuario", -1)
    fun isLoggedIn(): Boolean = getToken() != null

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
