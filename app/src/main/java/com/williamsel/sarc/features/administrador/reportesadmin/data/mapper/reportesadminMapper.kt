package com.williamsel.sarc.features.administrador.reportesadmin.data.mapper

import com.williamsel.sarc.core.database.entities.ReporteEntity
import com.williamsel.sarc.features.administrador.reportesadmin.data.models.ReporteAdminDto
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin


fun ReporteAdminDto.toEntity(): ReporteEntity {
    return ReporteEntity(
        idReporte = this.idReporte,
        titulo = this.titulo,
        descripcion = this.descripcion,
        imagen = this.imagen,
        latitud = this.latitud,
        longitud = this.longitud,
        ubicacion = this.ubicacion,
        idEstado = this.idEstado,
        nombreEstado = this.nombreEstado, // Nuevo
        idIncidencia = this.idIncidencia,
        nombreIncidencia = this.nombreIncidencia, // Nuevo
        idUsuario = this.idUsuario,
        nombreUsuario = this.nombreUsuario, // Nuevo
        fechaReporte = this.fecha
    )
}

fun ReporteEntity.toDomain(): ReporteAdmin {
    return ReporteAdmin(
        idReporte = this.idReporte,
        idUsuario = this.idUsuario ?: 0,
        nombreUsuario = this.nombreUsuario ?: "Usuario (Caché)",
        nombreIncidencia = this.nombreIncidencia ?: "Reporte",
        nombreEstado = this.nombreEstado ?: "Cargado offline",
        idIncidencia = this.idIncidencia ?: 0,
        titulo = this.titulo,
        descripcion = this.descripcion,
        ubicacion = this.ubicacion,
        latitud = this.latitud,
        longitud = this.longitud,
        idEstado = this.idEstado ?: 1,
        imagen = this.imagen,
        fecha = this.fechaReporte ?: "Sin fecha"
    )
}

fun ReporteAdminDto.toDomain(): ReporteAdmin {
    return ReporteAdmin(
        idReporte = this.idReporte,
        idUsuario = this.idUsuario,
        nombreUsuario = this.nombreUsuario,
        nombreIncidencia = this.nombreIncidencia,
        nombreEstado = this.nombreEstado,
        idIncidencia = this.idIncidencia,
        titulo = this.titulo,
        descripcion = this.descripcion,
        ubicacion = this.ubicacion,
        latitud = this.latitud,
        longitud = this.longitud,
        idEstado = this.idEstado,
        imagen = this.imagen,
        fecha = this.fecha
    )
}
