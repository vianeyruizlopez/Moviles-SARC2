package com.williamsel.sarc.features.administrador.reportesadmin.di

import com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api.ReportesAdminApi
import com.williamsel.sarc.features.administrador.reportesadmin.data.repositories.ReportesAdminRepositoryImpl
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportesAdminModule {

    @Provides
    @Singleton
    fun provideReportesAdminApi(retrofit: Retrofit): ReportesAdminApi {
        return retrofit.create(ReportesAdminApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReportesAdminRepository(
        api: ReportesAdminApi,
        dao: com.williamsel.sarc.core.database.dao.ReporteDao
    ): ReportesAdminRepository {
        return ReportesAdminRepositoryImpl(api, dao)
    }
}
