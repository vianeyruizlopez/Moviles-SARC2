package com.williamsel.sarc.features.ciudadano.detallereporteciu.data.repositories

import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.datasource.api.DetallereporteciuApi
import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities.Detallereporteciu
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.repositories.DetallereporteciuRepository
import javax.inject.Inject

class DetallereporteciuImpl @Inject constructor(
    private val api: DetallereporteciuApi
) : DetallereporteciuRepository {

    override suspend fun getDetalleReporte(id: Int): Result<Detallereporteciu> =
        runCatching { api.getDetalleReporte(id).toDomain() }
}
