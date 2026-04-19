package com.williamsel.sarc.features.ciudadano.mapaciu.data.repositories

import com.williamsel.sarc.features.ciudadano.mapaciu.data.datasource.api.MapaCiudadanoApi
import com.williamsel.sarc.features.ciudadano.mapaciu.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.repositories.MapaCiudadanoRepository
import javax.inject.Inject

class MapaCiudadanoRepositoryImpl @Inject constructor(
    private val api: MapaCiudadanoApi
) : MapaCiudadanoRepository {

    override suspend fun getReportes(idIncidencia: Int?, idEstado: Int?): List<ReporteMapa> {
        return try {
            api.getReportes(idIncidencia, idEstado).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
