package com.williamsel.sarc.features.administrador.paneldeadmin.domain.usecases

import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories.PanelDeAdminRepository
import javax.inject.Inject

class GetPanelDeAdminUseCase @Inject constructor(
    private val repository: PanelDeAdminRepository
) {
    suspend fun getReportes(idIncidencia: Int? = null, idEstado: Int? = null): List<PanelReporte> {
        return repository.getTodosReportes(idIncidencia, idEstado)
    }

    suspend fun actualizarEstado(idReporte: Int, idEstado: Int): PanelReporte {
        return repository.actualizarEstado(idReporte, idEstado)
    }

    suspend fun eliminarReporte(idReporte: Int) {
        repository.eliminarReporte(idReporte)
    }
}
