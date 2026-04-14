package com.williamsel.sarc.features.publico.login.di

import com.google.firebase.auth.FirebaseAuth
import com.williamsel.sarc.features.publico.login.data.repositories.LoginRepositoryImpl
import com.williamsel.sarc.features.publico.login.domain.repositories.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    companion object {
        @Provides
        @Singleton
        fun provideLoginApi(retrofit: Retrofit): com.williamsel.sarc.features.publico.login.data.datasource.api.LoginApi =
            retrofit.create(com.williamsel.sarc.features.publico.login.data.datasource.api.LoginApi::class.java)
    }
}
