package com.williamsel.sarc.core.di

import com.williamsel.sarc.core.hardware.data.AndroidCameraManager
import com.williamsel.sarc.core.hardware.data.AndroidLocationManager
import com.williamsel.sarc.core.hardware.data.AndroidNetworkManager
import com.williamsel.sarc.core.hardware.domain.CameraManager
import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.core.hardware.domain.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindCameraManager(
        androidCameraManager: AndroidCameraManager
    ): CameraManager

    @Binds
    @Singleton
    abstract fun bindLocationManager(
        androidLocationManager: AndroidLocationManager
    ): LocationManager

    @Binds
    @Singleton
    abstract fun bindNetworkManager(
        androidNetworkManager: AndroidNetworkManager
    ): NetworkManager
}
