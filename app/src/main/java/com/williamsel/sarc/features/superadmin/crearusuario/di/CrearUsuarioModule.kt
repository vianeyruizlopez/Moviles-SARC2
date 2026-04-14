package com.williamsel.sarc.features.superadmin.crearusuario.di

import com.williamsel.sarc.features.superadmin.crearusuario.data.datasource.api.CrearUsuarioApi
import com.williamsel.sarc.features.superadmin.crearusuario.data.repositories.CrearUsuarioRepositoryImpl
import com.williamsel.sarc.features.superadmin.crearusuario.domain.repositories.CrearUsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrearUsuarioModule {

    @Binds
    @Singleton
    abstract fun bindCrearUsuarioRepository(
        impl: CrearUsuarioRepositoryImpl
    ): CrearUsuarioRepository

    companion object {
        @Provides
        @Singleton
        fun provideCrearUsuarioApi(retrofit: Retrofit): CrearUsuarioApi =
            retrofit.create(CrearUsuarioApi::class.java)
    }
}
