package com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories

import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin

interface DetalleReporteAdminRepository {
    suspend fun getReporteById(idReporte: Int): DetalleReporteAdmin
    suspend fun actualizarEstado(idReporte: Int, idEstado: Int)
}
