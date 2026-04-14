package com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories

import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte

interface PanelDeAdminRepository {
    suspend fun getTodosReportes(idIncidencia: Int? = null, idEstado: Int? = null): List<PanelReporte>
    suspend fun actualizarEstado(idReporte: Int, idEstado: Int): PanelReporte
    suspend fun eliminarReporte(idReporte: Int)
}
