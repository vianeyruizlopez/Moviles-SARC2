package com.williamsel.sarc.features.perfil.domain.repository

import com.williamsel.sarc.features.perfil.domain.entities.Usuario

interface PerfilRepository {
    suspend fun sincronizarPerfil(id: Int): Boolean
    suspend fun getPerfilLocal(id: Int): Usuario?
}
