package com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories

import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte

interface PanelDeAdminRepository {
    suspend fun getResumenReportes(): Result<ResumenReportes>
    suspend fun getTodosReportes(idIncidencia: Int? = null, idEstado: Int? = null): Result<List<PanelReporte>>
    suspend fun buscarReportes(query: String): Result<List<PanelReporte>>
    suspend fun actualizarEstado(idReporte: Int, idEstado: Int): Result<Unit>
    suspend fun eliminarReporte(idReporte: Int): Result<Unit>
}
