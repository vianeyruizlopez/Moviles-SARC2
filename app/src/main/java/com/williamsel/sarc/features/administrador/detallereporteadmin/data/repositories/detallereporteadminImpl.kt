package com.williamsel.sarc.features.administrador.detallereporteadmin.data.repositories

import com.williamsel.sarc.features.administrador.detallereporteadmin.data.datasource.api.DetalleReporteAdminApi
import com.williamsel.sarc.features.administrador.detallereporteadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories.DetalleReporteAdminRepository
import javax.inject.Inject

class DetalleReporteAdminRepositoryImpl @Inject constructor(
    private val api: DetalleReporteAdminApi
) : DetalleReporteAdminRepository {

    override suspend fun getReporteById(idReporte: Int): DetalleReporteAdmin {
        return api.getReporteById(idReporte).toDomain()
    }
}
