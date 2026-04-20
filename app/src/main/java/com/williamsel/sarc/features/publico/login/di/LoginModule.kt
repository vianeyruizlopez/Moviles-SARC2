package com.williamsel.sarc.features.publico.login.di

import com.williamsel.sarc.features.publico.login.data.datasource.api.LoginApi
import com.williamsel.sarc.features.publico.login.data.repositories.LoginRepositoryImpl
import com.williamsel.sarc.features.publico.login.domain.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(repository: LoginRepositoryImpl): LoginRepository {
        return repository
    }
}
