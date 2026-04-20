package com.williamsel.sarc.features.perfil.di

import com.williamsel.sarc.features.perfil.data.datasource.api.PerfilApi
import com.williamsel.sarc.features.perfil.data.repository.PerfilRepositoryImpl
import com.williamsel.sarc.features.perfil.domain.repository.PerfilRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PerfilModule {

    @Binds
    @Singleton
    abstract fun bindPerfilRepository(
        impl: PerfilRepositoryImpl
    ): PerfilRepository

    companion object {
        @Provides
        @Singleton
        fun providePerfilApi(retrofit: Retrofit): PerfilApi {
            return retrofit.create(PerfilApi::class.java)
        }
    }
}