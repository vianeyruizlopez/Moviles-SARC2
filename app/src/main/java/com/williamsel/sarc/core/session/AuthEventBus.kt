package com.williamsel.sarc.core.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {

    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    sealed class AuthEvent {
        data object SessionExpired : AuthEvent()
    }

    fun emitSessionExpired() {
        _events.tryEmit(AuthEvent.SessionExpired)
    }
}