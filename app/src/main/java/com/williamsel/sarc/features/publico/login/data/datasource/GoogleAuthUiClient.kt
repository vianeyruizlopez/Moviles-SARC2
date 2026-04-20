package com.williamsel.sarc.features.publico.login.data.datasource

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthUiClient @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun beginSignIn(): IntentSenderRequest? = null
    suspend fun signInWithIntent(intent: Intent): GoogleSignInResult = 
        GoogleSignInResult.Error("Google Login desactivado (Arquitectura Limpia)")
    suspend fun getFreshIdToken(): String? = null
    fun isUserSignedIn(): Boolean = false
    suspend fun signOut() {}
}

sealed class GoogleSignInResult {
    data class Success(
        val uid: String,
        val email: String,
        val nombre: String,
        val photoUrl: String?,
        val idToken: String
    ) : GoogleSignInResult()
    data class Error(val mensaje: String) : GoogleSignInResult()
}
