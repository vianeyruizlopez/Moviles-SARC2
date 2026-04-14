package com.williamsel.sarc.core.hardware.domain

interface CameraManager {
    suspend fun tomarFoto(): ByteArray?
    fun tienePermisoCamera(): Boolean
}