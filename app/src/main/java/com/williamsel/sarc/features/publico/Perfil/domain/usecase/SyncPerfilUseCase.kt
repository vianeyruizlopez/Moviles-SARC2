package com.williamsel.sarc.features.perfil.domain.usecase

import com.williamsel.sarc.features.perfil.domain.repository.PerfilRepository
import javax.inject.Inject

class SyncPerfilUseCase @Inject constructor(
    private val repository: PerfilRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.sincronizarPerfil(id)
    }
}