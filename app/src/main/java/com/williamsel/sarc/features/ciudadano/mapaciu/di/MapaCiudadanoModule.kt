package com.williamsel.sarc.features.ciudadano.mapaciu.di

import com.williamsel.sarc.features.ciudadano.mapaciu.data.datasource.api.MapaCiudadanoApi
import com.williamsel.sarc.features.ciudadano.mapaciu.data.repositories.MapaCiudadanoRepositoryImpl
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.repositories.MapaCiudadanoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapaCiudadanoModule {

    @Binds
    @Singleton
    abstract fun bindMapaCiudadanoRepository(
        impl: MapaCiudadanoRepositoryImpl
    ): MapaCiudadanoRepository

    companion object {
        @Provides
        @Singleton
        fun provideMapaCiudadanoApi(retrofit: Retrofit): MapaCiudadanoApi =
            retrofit.create(MapaCiudadanoApi::class.java)
    }
}
