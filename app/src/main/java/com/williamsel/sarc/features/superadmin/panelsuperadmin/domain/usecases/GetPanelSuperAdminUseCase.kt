package com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.usecases

import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.entities.PanelSuperAdmin
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.repositories.PanelSuperAdminRepository
import javax.inject.Inject

class GetPanelSuperAdminUseCase @Inject constructor(
    private val repository: PanelSuperAdminRepository
) {
    suspend operator fun invoke(): PanelSuperAdmin =
        repository.getPanelData()
}
