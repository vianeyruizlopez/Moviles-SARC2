package com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.usecases

import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.EstadisticasGlobal
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.repositories.EstadisticasGlobalRepository
import javax.inject.Inject

class GetEstadisticasGlobalUseCase @Inject constructor(
    private val repository: EstadisticasGlobalRepository
) {
    suspend operator fun invoke(): EstadisticasGlobal =
        repository.getEstadisticas()
}
