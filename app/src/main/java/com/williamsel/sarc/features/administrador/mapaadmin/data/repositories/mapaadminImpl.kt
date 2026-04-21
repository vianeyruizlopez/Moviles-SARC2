package com.williamsel.sarc.features.administrador.mapaadmin.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.administrador.mapaadmin.data.datasource.api.MapaAdminApi
import com.williamsel.sarc.features.administrador.mapaadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.repositories.MapaAdminRepository
import javax.inject.Inject

class MapaAdminImpl @Inject constructor(
    private val api: MapaAdminApi,
    private val dao: ReporteDao
) : MapaAdminRepository {

    override suspend fun getReportesParaMapa(
        idIncidencia: Int?,
        idEstado: Int?
    ): Result<List<MapaReporte>> {
        return try {
            //api
            val remote = api.getReportesParaMapa(idIncidencia, idEstado)
            Result.success(remote.map { it.toDomain() })
        } catch (e: Exception) {
            //rooms
            try {

                val allCached = dao.getAllList()
                

                val filteredList = allCached.filter { entity ->

                    val matchIncidencia = idIncidencia == null || idIncidencia == 0 || entity.idIncidencia == idIncidencia

                    val matchEstado = idEstado == null || idEstado == 0 || entity.idEstado == idEstado
                    
                    matchIncidencia && matchEstado
                }.map { entity ->
                    MapaReporte(
                        idReporte = entity.idReporte,
                        idUsuario = entity.idUsuario ?: 0,
                        nombreUsuario = entity.nombreUsuario ?: "Usuario (Offline)",
                        titulo = entity.titulo,
                        descripcion = entity.descripcion,
                        ubicacion = entity.ubicacion ?: "Sin ubicación",
                        latitud = entity.latitud,
                        longitud = entity.longitud,
                        incidencia = CategoriaIncidencia.fromId(entity.idIncidencia ?: 0),
                        estado = EstadoMapaReporte.fromId(entity.idEstado ?: 1),
                        imagen = entity.imagen,
                        fecha = entity.fechaReporte ?: "Sin fecha"
                    )
                }
                
                Result.success(filteredList)
            } catch (dbError: Exception) {
                Result.failure(dbError)
            }
        }
    }
}
