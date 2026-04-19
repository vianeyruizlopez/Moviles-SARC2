package com.williamsel.sarc.features.ciudadano.mapaciu.domain.repositories

import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa

interface MapaCiudadanoRepository {
    suspend fun getReportes(idIncidencia: Int? = null, idEstado: Int? = null): List<ReporteMapa>
}
