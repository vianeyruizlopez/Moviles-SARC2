package com.williamsel.sarc.features.ciudadano.detallereporteciu.di

import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.datasource.api.DetallereporteciuApi
import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.repositories.DetallereporteciuImpl
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.repositories.DetallereporteciuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetallereporteciuModule {

    @Provides
    @Singleton
    fun provideDetallereporteciuApi(retrofit: Retrofit): DetallereporteciuApi =
        retrofit.create(DetallereporteciuApi::class.java)

    @Provides
    @Singleton
    fun provideDetallereporteciuRepository(
        impl: DetallereporteciuImpl
    ): DetallereporteciuRepository = impl
}
