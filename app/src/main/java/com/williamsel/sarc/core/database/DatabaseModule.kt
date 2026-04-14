package com.williamsel.sarc.core.database

import android.content.Context
import androidx.room.Room
import com.williamsel.sarc.core.database.SarcDatabase
import com.williamsel.sarc.core.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSarcDatabase(
        @ApplicationContext context: Context
    ): SarcDatabase {
        return Room.databaseBuilder(
            context,
            SarcDatabase::class.java,
            "sarc_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRolDao(db: SarcDatabase): RolDao = db.rolDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(db: SarcDatabase): UsuarioDao = db.usuarioDao()

    @Provides
    @Singleton
    fun provideCatIncidenciasDao(db: SarcDatabase): CatIncidenciasDao = db.catIncidenciasDao()

    @Provides
    @Singleton
    fun provideCatEstadoReporteDao(db: SarcDatabase): CatEstadoReporteDao = db.catEstadoReporteDao()

    @Provides
    @Singleton
    fun provideReporteDao(db: SarcDatabase): ReporteDao = db.reporteDao()

}