package com.williamsel.sarc.features.ciudadano.misreportesciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories.MisReportesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMisReportesUseCase @Inject constructor(
    private val repository: MisReportesRepository
) {
    operator fun invoke(idUsuario: Int): Flow<List<ReporteCiudadano>> =
        repository.getReportesByUsuario(idUsuario)
}
