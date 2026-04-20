package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.repositories

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.datasource.api.PanelPrincipalAdminApi
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.repositories.PanelPrincipalAdminRepository
import javax.inject.Inject

class PanelPrincipalAdminImpl @Inject constructor(
    private val api: PanelPrincipalAdminApi
) : PanelPrincipalAdminRepository {

    override suspend fun getResumenReportes(): Result<ResumenReportes> =
        runCatching { api.getResumenReportes().toDomain() }
}
