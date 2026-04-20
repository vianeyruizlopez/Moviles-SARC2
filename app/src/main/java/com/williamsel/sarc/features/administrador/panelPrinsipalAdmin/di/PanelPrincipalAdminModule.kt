package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.di

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.datasource.api.PanelPrincipalAdminApi
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.repositories.PanelPrincipalAdminImpl
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.repositories.PanelPrincipalAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PanelPrincipalAdminModule {

    @Provides
    @Singleton
    fun providePanelPrincipalAdminApi(retrofit: Retrofit): PanelPrincipalAdminApi =
        retrofit.create(PanelPrincipalAdminApi::class.java)

    @Provides
    @Singleton
    fun providePanelPrincipalAdminRepository(
        impl: PanelPrincipalAdminImpl
    ): PanelPrincipalAdminRepository = impl
}
