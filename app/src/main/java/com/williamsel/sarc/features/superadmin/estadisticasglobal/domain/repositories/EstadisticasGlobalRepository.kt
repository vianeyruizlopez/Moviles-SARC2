package com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.repositories

import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.EstadisticasGlobal

interface EstadisticasGlobalRepository {
    suspend fun getEstadisticas(): EstadisticasGlobal
}
