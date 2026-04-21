package com.williamsel.sarc.features.superadmin.estadisticasglobal.data.repositories

import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.datasource.api.EstadisticasGlobalApi
import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.mapper.toDomain
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.EstadisticasGlobal
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.repositories.EstadisticasGlobalRepository
import javax.inject.Inject

class EstadisticasGlobalRepositoryImpl @Inject constructor(
    private val api: EstadisticasGlobalApi
) : EstadisticasGlobalRepository {

    override suspend fun getEstadisticas(): EstadisticasGlobal {
        return api.getEstadisticas().toDomain()
    }
}
