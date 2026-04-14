package com.williamsel.sarc.features.ciudadano.crearreportes.data.repositories

import com.williamsel.sarc.core.database.dao.CatIncidenciasDao
import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.ciudadano.crearreportes.data.datasource.api.CrearReportesApi
import com.williamsel.sarc.features.ciudadano.crearreportes.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.repositories.CrearReportesRepository
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CrearReportesRepositoryImpl @Inject constructor(
    private val api: CrearReportesApi,
    private val reporteDao: ReporteDao,
    private val catIncidenciasDao: CatIncidenciasDao
) : CrearReportesRepository {

    override suspend fun enviarReporte(idUsuario: Int, reporte: NuevoReporte): Result<Int> {
        return try {
            val textType = "text/plain".toMediaTypeOrNull()

            val tituloPart      = RequestBody.create(textType, reporte.titulo)
            val descripcionPart = RequestBody.create(textType, reporte.descripcion)
            val idUsuarioPart   = RequestBody.create(textType, idUsuario.toString())
            val idIncidenciaPart = RequestBody.create(textType, reporte.idIncidencia.toString())
            val latitudPart     = RequestBody.create(textType, (reporte.latitud ?: 0.0).toString())
            val longitudPart    = RequestBody.create(textType, (reporte.longitud ?: 0.0).toString())
            val idEstadoPart    = RequestBody.create(textType, "1")
            val ubicacionPart   = RequestBody.create(textType, reporte.ubicacion)

            val imagenPart: MultipartBody.Part? = reporte.imagenBytes?.let { bytes ->
                MultipartBody.Part.createFormData(
                    "imagen",
                    "reporte.jpg",
                    bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )
            }

            val response = api.crearReporte(
                titulo       = tituloPart,
                descripcion  = descripcionPart,
                idUsuario    = idUsuarioPart,
                idIncidencia = idIncidenciaPart,
                latitud      = latitudPart,
                longitud     = longitudPart,
                idEstado     = idEstadoPart,
                ubicacion    = ubicacionPart,
                imagen       = imagenPart
            )

            Result.success(response.idReportes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategorias(): List<CategoriaIncidencia> {
        return try {
            api.getCategorias().map { it.toDomain() }
        } catch (e: Exception) {
            catIncidenciasDao.getAll().first().map { entity ->
                CategoriaIncidencia(
                    id     = entity.idIncidencia,
                    nombre = entity.nombre,
                    emoji  = when (entity.nombre.lowercase()) {
                        "bache"     -> "🚧"
                        "basura"    -> "🗑️"
                        "alumbrado" -> "💡"
                        else        -> "📋"
                    }
                )
            }
        }
    }
}
