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

            val response = api.crearReporte(
                titulo       = reporte.titulo.toRequestBody(textType),
                descripcion  = reporte.descripcion.toRequestBody(textType),
                idUsuario    = idUsuario.toString().toRequestBody(textType),
                idIncidencia = reporte.idIncidencia.toString().toRequestBody(textType),
                latitud      = (reporte.latitud ?: 0.0).toString().toRequestBody(textType),
                longitud     = (reporte.longitud ?: 0.0).toString().toRequestBody(textType),
                idEstado     = "1".toRequestBody(textType),
                ubicacion    = reporte.ubicacion.toRequestBody(textType),
                imagen       = reporte.imagenBytes?.let {
                    MultipartBody.Part.createFormData("imagen", "reporte.jpg", it.toRequestBody("image/jpeg".toMediaTypeOrNull()))
                }
            )
            Result.success(response.idReportes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategorias(): List<CategoriaIncidencia> {
        return try {
            val apiCats = api.getCategorias().map { it.toDomain() }
            if (apiCats.isNotEmpty()) apiCats else getLocalCategorias()
        } catch (e: Exception) {
            getLocalCategorias()
        }
    }

    private suspend fun getLocalCategorias(): List<CategoriaIncidencia> {
        return listOf(
            CategoriaIncidencia(1, "Bache", "🚧"),
            CategoriaIncidencia(2, "Basura", "🗑️"),
            CategoriaIncidencia(3, "Alumbrado", "💡")
        )
    }
}