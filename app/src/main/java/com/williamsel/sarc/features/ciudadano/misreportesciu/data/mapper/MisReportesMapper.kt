package com.williamsel.sarc.features.ciudadano.misreportesciu.data.mapper

import com.williamsel.sarc.core.database.entities.ReporteEntity
import com.williamsel.sarc.features.ciudadano.misreportesciu.data.models.ReporteCiudadanoDto
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun ReporteCiudadanoDto.toDomain(): ReporteCiudadano {
    val timestamp = try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(fecha)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }

    return ReporteCiudadano(
        idReporte = idReporte,
        titulo = titulo,
        descripcion = descripcion,
        ubicacion = ubicacion,
        fechaReporte = timestamp,
        incidencia = nombreIncidencia,
        estado = nombreEstado,
        idEstado = idEstado,
        puedeEditar = idEstado == 1
    )
}

fun ReporteEntity.toDomain(
    nombreIncidencia: String = "Otro",
    nombreEstado: String = when (idEstado) {
        1    -> "Pendiente"
        2    -> "En Proceso"
        3    -> "Resuelto"
        else -> "Desconocido"
    }
): ReporteCiudadano = ReporteCiudadano(
    idReporte = idReporte,
    titulo = titulo,
    descripcion = descripcion,
    ubicacion = ubicacion ?: "",
    fechaReporte = fechaReporte ?: 0L,
    incidencia = nombreIncidencia,
    estado = nombreEstado,
    idEstado = idEstado ?: 1,
    puedeEditar = idEstado == 1
)