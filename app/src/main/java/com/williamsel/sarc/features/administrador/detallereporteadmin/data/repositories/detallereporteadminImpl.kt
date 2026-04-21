package com.williamsel.sarc.features.administrador.detallereporteadmin.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.administrador.detallereporteadmin.data.datasource.api.DetalleReporteAdminApi
import com.williamsel.sarc.features.administrador.detallereporteadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories.DetalleReporteAdminRepository
import javax.inject.Inject

class DetalleReporteAdminRepositoryImpl @Inject constructor(
    private val api: DetalleReporteAdminApi,
    private val dao: ReporteDao
) : DetalleReporteAdminRepository {

    override suspend fun getReporteById(idReporte: Int): DetalleReporteAdmin {
        return try {
           //api
            api.getReporteById(idReporte).toDomain()
        } catch (e: Exception) {
            //rooms
            val entity = dao.getById(idReporte)
            if (entity != null) {
                DetalleReporteAdmin(
                    idReporte = entity.idReporte,
                    idUsuario = entity.idUsuario ?: 0,
                    nombreUsuario = entity.nombreUsuario ?: "Usuario (Offline)",
                    titulo = entity.titulo,
                    descripcion = entity.descripcion,
                    ubicacion = entity.ubicacion ?: "Sin ubicación",
                    latitud = entity.latitud,
                    longitud = entity.longitud,
                    idIncidencia = entity.idIncidencia ?: 0,
                    nombreIncidencia = entity.nombreIncidencia ?: "Reporte",
                    idEstado = entity.idEstado ?: 1,
                    nombreEstado = entity.nombreEstado ?: "Offline",
                    imagen = entity.imagen,
                    fecha = entity.fechaReporte ?: "Sin fecha"
                )
            } else {
                throw e
            }
        }
    }

    override suspend fun actualizarEstado(idReporte: Int, idEstado: Int) {
        try {
            api.actualizarEstado(idReporte, mapOf("id_estado" to idEstado))

            dao.updateEstado(idReporte, idEstado)
        } catch (e: Exception) {
            dao.updateEstado(idReporte, idEstado)
            throw e
        }
    }
}
