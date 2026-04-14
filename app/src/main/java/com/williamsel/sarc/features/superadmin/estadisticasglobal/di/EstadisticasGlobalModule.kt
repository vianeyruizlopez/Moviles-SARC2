package com.williamsel.sarc.features.superadmin.estadisticasglobal.di

import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.datasource.api.EstadisticasGlobalApi
import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.repositories.EstadisticasGlobalRepositoryImpl
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.repositories.EstadisticasGlobalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EstadisticasGlobalModule {

    @Binds
    @Singleton
    abstract fun bindEstadisticasGlobalRepository(
        impl: EstadisticasGlobalRepositoryImpl
    ): EstadisticasGlobalRepository

    companion object {
        @Provides
        @Singleton
        fun provideEstadisticasGlobalApi(retrofit: Retrofit): EstadisticasGlobalApi =
            retrofit.create(EstadisticasGlobalApi::class.java)
    }
}
