package com.williamsel.sarc.features.ciudadano.crearreportes.di

import com.williamsel.sarc.features.ciudadano.crearreportes.data.datasource.api.CrearReportesApi
import com.williamsel.sarc.features.ciudadano.crearreportes.data.repositories.CrearReportesRepositoryImpl
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.repositories.CrearReportesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrearReportesModule {

    @Binds
    @Singleton
    abstract fun bindCrearReportesRepository(
        impl: CrearReportesRepositoryImpl
    ): CrearReportesRepository

    companion object {
        @Provides
        @Singleton
        fun provideCrearReportesApi(retrofit: Retrofit): CrearReportesApi =
            retrofit.create(CrearReportesApi::class.java)
    }
}
