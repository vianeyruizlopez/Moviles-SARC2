package com.williamsel.sarc.features.superadmin.panelsuperadmin.di

import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.datasource.api.PanelSuperAdminApi
import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.repositories.PanelSuperAdminRepositoryImpl
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.repositories.PanelSuperAdminRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PanelSuperAdminModule {

    @Binds
    @Singleton
    abstract fun bindPanelSuperAdminRepository(
        impl: PanelSuperAdminRepositoryImpl
    ): PanelSuperAdminRepository

    companion object {
        @Provides
        @Singleton
        fun providePanelSuperAdminApi(retrofit: Retrofit): PanelSuperAdminApi =
            retrofit.create(PanelSuperAdminApi::class.java)
    }
}
