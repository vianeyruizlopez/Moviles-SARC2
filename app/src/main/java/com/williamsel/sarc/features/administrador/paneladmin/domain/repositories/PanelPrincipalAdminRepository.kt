package com.williamsel.sarc.features.administrador.paneladmin.domain.repositories

import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes

interface PanelPrincipalAdminRepository {
    suspend fun getResumenReportes(): Result<ResumenReportes>
}
