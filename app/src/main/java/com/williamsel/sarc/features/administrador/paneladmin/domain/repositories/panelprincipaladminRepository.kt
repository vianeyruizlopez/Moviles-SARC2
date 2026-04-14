package com.williamsel.sarc.features.administrador.paneladmin.domain.repositories

import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.Reporte
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes

interface PanelPrincipalAdminRepository {
    suspend fun getResumenReportes(): Result<ResumenReportes>
    suspend fun getReportes(): Result<List<Reporte>>
    suspend fun getReporteById(id: Int): Result<Reporte>
}
