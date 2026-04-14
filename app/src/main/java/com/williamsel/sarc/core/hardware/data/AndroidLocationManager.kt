package com.williamsel.sarc.core.hardware.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.core.hardware.domain.model.UbicacionModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class AndroidLocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationManager {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun tienePermisoUbicacion(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @Suppress("MissingPermission")
    override suspend fun obtenerUbicacion(): UbicacionModel? {
        if (!tienePermisoUbicacion()) return null

        return try {
            val cancellationToken = CancellationTokenSource()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationToken.token
            ).await()

            location?.let {
                val direccion = obtenerDireccion(it.latitude, it.longitude)
                UbicacionModel(
                    latitud = it.latitude,
                    longitud = it.longitude,
                    direccion = direccion
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun obtenerDireccion(lat: Double, lon: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale("es", "MX"))
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            addresses?.firstOrNull()?.getAddressLine(0)
        } catch (e: Exception) {
            null
        }
    }
}