package com.williamsel.sarc.core.hardware.domain

interface NetworkManager {
    fun isNetworkAvailable(): Boolean
}