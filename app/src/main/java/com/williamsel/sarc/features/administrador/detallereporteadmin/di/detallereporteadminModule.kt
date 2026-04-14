package com.williamsel.sarc.features.administrador.detallereporteadmin.di

import com.williamsel.sarc.features.administrador.detallereporteadmin.data.datasource.api.DetalleReporteAdminApi
import com.williamsel.sarc.features.administrador.detallereporteadmin.data.repositories.DetalleReporteAdminRepositoryImpl
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories.DetalleReporteAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetalleReporteAdminModule {

    @Provides
    @Singleton
    fun provideDetalleReporteAdminApi(retrofit: Retrofit): DetalleReporteAdminApi {
        return retrofit.create(DetalleReporteAdminApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDetalleReporteAdminRepository(
        api: DetalleReporteAdminApi
    ): DetalleReporteAdminRepository {
        return DetalleReporteAdminRepositoryImpl(api)
    }
}
