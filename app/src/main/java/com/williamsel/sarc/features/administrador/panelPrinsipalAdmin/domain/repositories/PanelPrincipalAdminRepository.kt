package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.repositories

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes

interface PanelPrincipalAdminRepository {
    suspend fun getResumenReportes(): Result<ResumenReportes>
}
