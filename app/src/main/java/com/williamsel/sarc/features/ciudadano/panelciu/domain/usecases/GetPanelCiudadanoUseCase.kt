package com.williamsel.sarc.features.ciudadano.panelciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.panelciu.domain.entities.PanelCiudadano
import com.williamsel.sarc.features.ciudadano.panelciu.domain.repositories.PanelCiudadanoRepository
import javax.inject.Inject

class GetPanelCiudadanoUseCase @Inject constructor(
    private val repository: PanelCiudadanoRepository
) {
    suspend operator fun invoke(idUsuario: Int): PanelCiudadano =
        repository.getPanelData(idUsuario)
}
