package com.williamsel.sarc.core.network

import com.williamsel.sarc.core.session.AuthEventBus
import com.williamsel.sarc.core.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            sessionManager.clearSession()
            AuthEventBus.emitSessionExpired()
        }

        return response
    }
}