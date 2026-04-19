package com.williamsel.sarc.features.administrador.paneladmin.data.repositories

import com.williamsel.sarc.features.administrador.paneladmin.data.datasource.api.PanelPrincipalAdminApi
import com.williamsel.sarc.features.administrador.paneladmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneladmin.domain.repositories.PanelPrincipalAdminRepository
import javax.inject.Inject

class PanelPrincipalAdminImpl @Inject constructor(
    private val api: PanelPrincipalAdminApi
) : PanelPrincipalAdminRepository {

    override suspend fun getResumenReportes(): Result<ResumenReportes> =
        runCatching { api.getResumenReportes().toDomain() }
}
