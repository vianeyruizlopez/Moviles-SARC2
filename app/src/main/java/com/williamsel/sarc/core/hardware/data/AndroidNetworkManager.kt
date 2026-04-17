package com.williamsel.sarc.core.hardware.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.williamsel.sarc.core.hardware.domain.NetworkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidNetworkManager @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkManager {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}