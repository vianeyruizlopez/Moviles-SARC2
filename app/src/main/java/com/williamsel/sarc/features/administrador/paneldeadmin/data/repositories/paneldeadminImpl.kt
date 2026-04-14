package com.williamsel.sarc.features.administrador.paneldeadmin.data.repositories

import com.williamsel.sarc.features.administrador.paneldeadmin.data.datasource.api.PanelDeAdminApi
import com.williamsel.sarc.features.administrador.paneldeadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories.PanelDeAdminRepository
import javax.inject.Inject

class PanelDeAdminRepositoryImpl @Inject constructor(
    private val api: PanelDeAdminApi
) : PanelDeAdminRepository {

    override suspend fun getTodosReportes(
        idIncidencia: Int?,
        idEstado: Int?
    ): List<PanelReporte> {
        return api.getTodosReportes(idIncidencia, idEstado).map { it.toDomain() }
    }

    override suspend fun actualizarEstado(idReporte: Int, idEstado: Int): PanelReporte {
        return api.actualizarEstado(idReporte, mapOf("id_estado" to idEstado)).toDomain()
    }

    override suspend fun eliminarReporte(idReporte: Int) {
        api.eliminarReporte(idReporte)
    }
}
