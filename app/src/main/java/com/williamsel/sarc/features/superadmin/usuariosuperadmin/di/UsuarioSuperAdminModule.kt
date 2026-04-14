package com.williamsel.sarc.features.superadmin.usuariosuperadmin.di

import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.datasource.api.UsuarioSuperAdminApi
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.repositories.UsuarioSuperAdminRepositoryImpl
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.repositories.UsuarioSuperAdminRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsuarioSuperAdminModule {

    @Binds
    @Singleton
    abstract fun bindUsuarioSuperAdminRepository(
        impl: UsuarioSuperAdminRepositoryImpl
    ): UsuarioSuperAdminRepository

    companion object {
        @Provides
        @Singleton
        fun provideUsuarioSuperAdminApi(retrofit: Retrofit): UsuarioSuperAdminApi =
            retrofit.create(UsuarioSuperAdminApi::class.java)
    }
}
