package com.williamsel.sarc.features.administrador.mapaadmin.di

import com.williamsel.sarc.features.administrador.mapaadmin.data.datasource.api.MapaAdminApi
import com.williamsel.sarc.features.administrador.mapaadmin.data.repositories.MapaAdminImpl
import com.williamsel.sarc.features.administrador.mapaadmin.domain.repositories.MapaAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapaAdminModule {

    @Provides
    @Singleton
    fun provideMapaAdminApi(retrofit: Retrofit): MapaAdminApi =
        retrofit.create(MapaAdminApi::class.java)

    @Provides
    @Singleton
    fun provideMapaAdminRepository(impl: MapaAdminImpl): MapaAdminRepository = impl
}
