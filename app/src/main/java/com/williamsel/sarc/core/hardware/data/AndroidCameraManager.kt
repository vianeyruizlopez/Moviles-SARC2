package com.williamsel.sarc.core.hardware.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.williamsel.sarc.core.hardware.domain.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    override fun tienePermisoCamera(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun tomarFoto(): ByteArray? {
        return null
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}