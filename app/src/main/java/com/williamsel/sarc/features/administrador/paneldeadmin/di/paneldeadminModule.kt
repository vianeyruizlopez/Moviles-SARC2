package com.williamsel.sarc.features.administrador.paneldeadmin.di

import com.williamsel.sarc.features.administrador.paneldeadmin.data.datasource.api.PanelDeAdminApi
import com.williamsel.sarc.features.administrador.paneldeadmin.data.repositories.PanelDeAdminRepositoryImpl
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories.PanelDeAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PanelDeAdminModule {

    @Provides
    @Singleton
    fun providePanelDeAdminApi(retrofit: Retrofit): PanelDeAdminApi {
        return retrofit.create(PanelDeAdminApi::class.java)
    }

    @Provides
    @Singleton
    fun providePanelDeAdminRepository(
        api: PanelDeAdminApi
    ): PanelDeAdminRepository {
        return PanelDeAdminRepositoryImpl(api)
    }
}
