package com.williamsel.sarc.features.administrador.paneldeadmin.domain.usecases

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories.PanelDeAdminRepository
import javax.inject.Inject

class GetPanelDeAdminUseCase @Inject constructor(
    private val repository: PanelDeAdminRepository
) {
    suspend fun getResumenReportes(): Result<ResumenReportes> =
        repository.getResumenReportes()

    suspend fun getReportes(idIncidencia: Int? = null, idEstado: Int? = null): Result<List<PanelReporte>> =
        repository.getTodosReportes(idIncidencia, idEstado)

    suspend fun buscarReportes(query: String): Result<List<PanelReporte>> =
        repository.buscarReportes(query)

    suspend fun actualizarEstado(idReporte: Int, idEstado: Int): Result<Unit> =
        repository.actualizarEstado(idReporte, idEstado)

    suspend fun eliminarReporte(idReporte: Int): Result<Unit> =
        repository.eliminarReporte(idReporte)
}
