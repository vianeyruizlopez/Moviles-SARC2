package com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.repositories

import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities.Detallereporteciu

interface DetallereporteciuRepository {
    suspend fun getDetalleReporte(id: Int): Result<Detallereporteciu>
}
