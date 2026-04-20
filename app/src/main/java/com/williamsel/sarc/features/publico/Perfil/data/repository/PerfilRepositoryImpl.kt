package com.williamsel.sarc.features.perfil.data.repository

import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.features.perfil.data.datasource.api.PerfilApi
import com.williamsel.sarc.features.perfil.data.mapper.toDomain
import com.williamsel.sarc.features.perfil.data.mapper.toEntity
import com.williamsel.sarc.features.perfil.domain.entities.Usuario
import com.williamsel.sarc.features.perfil.domain.repository.PerfilRepository
import javax.inject.Inject

class PerfilRepositoryImpl @Inject constructor(
    private val api: PerfilApi,
    private val dao: UsuarioDao
) : PerfilRepository {

    override suspend fun sincronizarPerfil(id: Int): Boolean {
        return try {
            val perfilDto = api.getPerfil(id)
            dao.insert(perfilDto.toEntity())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getPerfilLocal(id: Int): Usuario? {
        return dao.getById(id)?.toDomain()
    }
}