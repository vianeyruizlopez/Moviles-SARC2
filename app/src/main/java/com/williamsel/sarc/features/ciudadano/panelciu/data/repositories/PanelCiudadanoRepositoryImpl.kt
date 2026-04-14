package com.williamsel.sarc.features.ciudadano.panelciu.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.features.ciudadano.panelciu.data.datasource.api.PanelCiudadanoApi
import com.williamsel.sarc.features.ciudadano.panelciu.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.panelciu.domain.entities.PanelCiudadano
import com.williamsel.sarc.features.ciudadano.panelciu.domain.repositories.PanelCiudadanoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PanelCiudadanoRepositoryImpl @Inject constructor(
    private val api: PanelCiudadanoApi,
    private val usuarioDao: UsuarioDao,
    private val reporteDao: ReporteDao
) : PanelCiudadanoRepository {

    override suspend fun getPanelData(idUsuario: Int): PanelCiudadano {
        return try {
            api.getPanelData(idUsuario).toDomain()
        } catch (e: Exception) {
            val usuario = usuarioDao.getById(idUsuario)
            val reportes = reporteDao.getByUsuario(idUsuario).first()
            PanelCiudadano(
                idUsuario      = idUsuario,
                nombreCompleto = usuario?.let {
                    "${it.nombre} ${it.primerApellido} ${it.segundoApellido}".trim()
                } ?: "Usuario",
                totalReportes  = reportes.size,
                pendientes     = reportes.count { it.idEstado == 1 },
                enProceso      = reportes.count { it.idEstado == 2 },
                resueltos      = reportes.count { it.idEstado == 3 }
            )
        }
    }
}
