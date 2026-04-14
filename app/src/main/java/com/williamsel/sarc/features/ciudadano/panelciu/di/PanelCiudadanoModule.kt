package com.williamsel.sarc.features.ciudadano.panelciu.di

import com.williamsel.sarc.features.ciudadano.panelciu.data.datasource.api.PanelCiudadanoApi
import com.williamsel.sarc.features.ciudadano.panelciu.data.repositories.PanelCiudadanoRepositoryImpl
import com.williamsel.sarc.features.ciudadano.panelciu.domain.repositories.PanelCiudadanoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PanelCiudadanoModule {

    @Binds
    @Singleton
    abstract fun bindPanelCiudadanoRepository(
        impl: PanelCiudadanoRepositoryImpl
    ): PanelCiudadanoRepository

    companion object {
        @Provides
        @Singleton
        fun providePanelCiudadanoApi(retrofit: Retrofit): PanelCiudadanoApi =
            retrofit.create(PanelCiudadanoApi::class.java)
    }
}
