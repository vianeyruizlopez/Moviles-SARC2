package com.williamsel.sarc.features.ciudadano.mapaciu.domain.repositories

import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa

interface MapaCiudadanoRepository {
    suspend fun getReportes(): List<ReporteMapa>
    suspend fun getReportesFiltrados(idIncidencia: Int?, idEstado: Int?): List<ReporteMapa>
}
