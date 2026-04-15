package com.williamsel.sarc.features.ciudadano.registro.di

import com.williamsel.sarc.features.ciudadano.registro.data.datasource.api.RegistroApi
import com.williamsel.sarc.features.ciudadano.registro.data.repositories.RegistroRepositoryImpl
import com.williamsel.sarc.features.ciudadano.registro.domain.repositories.RegistroRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RegistroModule {

    @Binds
    @Singleton
    abstract fun bindRegistroRepository(
        impl: RegistroRepositoryImpl
    ): RegistroRepository

    companion object {
        @Provides
        @Singleton
        fun provideRegistroApi(retrofit: Retrofit): RegistroApi =
            retrofit.create(RegistroApi::class.java)
    }
}
