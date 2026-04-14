package com.williamsel.sarc.features.ciudadano.misreportesciu.di

import com.williamsel.sarc.features.ciudadano.misreportesciu.data.datasource.api.MisReportesApi
import com.williamsel.sarc.features.ciudadano.misreportesciu.data.repositories.MisReportesRepositoryImpl
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories.MisReportesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MisReportesModule {

    @Binds
    @Singleton
    abstract fun bindMisReportesRepository(
        impl: MisReportesRepositoryImpl
    ): MisReportesRepository

    companion object {
        @Provides
        @Singleton
        fun provideMisReportesApi(retrofit: Retrofit): MisReportesApi =
            retrofit.create(MisReportesApi::class.java)
    }
}
