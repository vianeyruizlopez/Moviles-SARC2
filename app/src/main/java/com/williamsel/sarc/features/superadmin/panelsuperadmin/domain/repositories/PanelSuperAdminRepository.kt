package com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.repositories

import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.entities.PanelSuperAdmin

interface PanelSuperAdminRepository {
    suspend fun getPanelData(): PanelSuperAdmin
}
