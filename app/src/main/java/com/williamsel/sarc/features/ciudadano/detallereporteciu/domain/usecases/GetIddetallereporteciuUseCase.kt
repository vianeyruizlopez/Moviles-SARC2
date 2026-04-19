package com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities.Detallereporteciu
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.repositories.DetallereporteciuRepository
import javax.inject.Inject

class GetIddetallereporteciuUseCase @Inject constructor(
    private val repository: DetallereporteciuRepository
) {
    suspend operator fun invoke(id: Int): Result<Detallereporteciu> =
        repository.getDetalleReporte(id)
}
