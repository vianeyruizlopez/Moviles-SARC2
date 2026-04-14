package com.williamsel.sarc.features.administrador.paneladmin.data.repositories

import com.williamsel.sarc.features.administrador.paneladmin.data.datasource.api.PanelPrincipalAdminApi
import com.williamsel.sarc.features.administrador.paneladmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.Reporte
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneladmin.domain.repositories.PanelPrincipalAdminRepository
import javax.inject.Inject

class PanelPrincipalAdminImpl @Inject constructor(
    private val api: PanelPrincipalAdminApi
) : PanelPrincipalAdminRepository {

    override suspend fun getResumenReportes(): Result<ResumenReportes> =
        runCatching { api.getResumenReportes().toDomain() }

    override suspend fun getReportes(): Result<List<Reporte>> =
        runCatching { api.getReportes().map { it.toDomain() } }

    override suspend fun getReporteById(id: Int): Result<Reporte> =
        runCatching { api.getReporteById(id).toDomain() }
}
