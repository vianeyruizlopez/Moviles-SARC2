package com.williamsel.sarc.features.administrador.reportesadmin.data.mapper

import com.williamsel.sarc.core.database.entities.ReporteEntity
import com.williamsel.sarc.features.administrador.reportesadmin.data.models.ReporteAdminDto
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin


 ///se convierte un DTO a una Entidad de Room para chache
fun ReporteAdminDto.toEntity(): ReporteEntity {
    return ReporteEntity(
        idReporte = this.idReporte,
        titulo = this.titulo,
        descripcion = this.descripcion,
        imagen = this.imagen,
        latitud = this.latitud ?: 0.0,
        longitud = this.longitud ?: 0.0,
        ubicacion = this.ubicacion,
        idEstado = this.idEstado,
        idIncidencia = this.idIncidencia,
        idUsuario = this.idUsuario,
        fechaReporte = System.currentTimeMillis()
    )
}

//conierte de entidad de room a dominio
fun ReporteEntity.toDomain(): ReporteAdmin {
    return ReporteAdmin(
        idReporte = this.idReporte,
        idUsuario = this.idUsuario ?: 0,
        nombreUsuario = "Usuario (Caché)",
        nombreIncidencia = "Reporte",
        nombreEstado = "Cargado offline",
        idIncidencia = this.idIncidencia ?: 0,
        titulo = this.titulo,
        descripcion = this.descripcion,
        ubicacion = this.ubicacion,
        latitud = this.latitud,
        longitud = this.longitud,
        idEstado = this.idEstado ?: 1,
        imagen = this.imagen,
        fecha = "Offline"
    )
}

///conviette de DTO a dominio cuando se tenga internet
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
