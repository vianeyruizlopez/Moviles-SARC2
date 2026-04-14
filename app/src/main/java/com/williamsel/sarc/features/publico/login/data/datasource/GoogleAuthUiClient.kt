package com.williamsel.sarc.features.publico.login.data.datasource

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.williamsel.sarc.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GoogleAuthUiClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth
) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    suspend fun beginSignIn(): IntentSenderRequest? {
        return try {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(context.getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(false)
                .build()

            val result = oneTapClient.beginSignIn(signInRequest).await()
            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): GoogleSignInResult {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
                ?: return GoogleSignInResult.Error("No se obtuvo idToken de Google")

            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = auth.signInWithCredential(firebaseCredential).await()
            val user = authResult.user
                ?: return GoogleSignInResult.Error("Firebase no devolvió usuario")

            val freshToken = user.getIdToken(true).await().token
                ?: return GoogleSignInResult.Error("No se pudo obtener idToken de Firebase")

            GoogleSignInResult.Success(
                uid      = user.uid,
                email    = user.email ?: "",
                nombre   = user.displayName ?: "",
                photoUrl = user.photoUrl?.toString(),
                idToken  = freshToken
            )
        } catch (e: Exception) {
            GoogleSignInResult.Error(e.message ?: "Error desconocido al autenticar con Google")
        }
    }

    suspend fun getFreshIdToken(): String? {
        return try {
            val user = auth.currentUser ?: return null
            user.getIdToken(true).await().token
        } catch (e: Exception) {
            null
        }
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    suspend fun signOut() {
        try { oneTapClient.signOut().await() } catch (_: Exception) {}
        auth.signOut()
    }
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
