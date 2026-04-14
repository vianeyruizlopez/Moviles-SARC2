package com.williamsel.sarc.features.ciudadano.crearreportes.domain.repositories

import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte

interface CrearReportesRepository {
    suspend fun enviarReporte(idUsuario: Int, reporte: NuevoReporte): Result<Int>
    suspend fun getCategorias(): List<CategoriaIncidencia>
}
