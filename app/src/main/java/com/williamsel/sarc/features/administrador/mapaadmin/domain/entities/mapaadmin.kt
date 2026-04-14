package com.williamsel.sarc.features.administrador.mapaadmin.domain.entities

data class MapaReporte(
    val idReporte: Int,
    val idUsuario: Int,
    val nombreUsuario: String,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val latitud: Double,
    val longitud: Double,
    val incidencia: CategoriaIncidencia,
    val estado: EstadoMapaReporte,
    val imagen: String?,
    val fecha: String
)

enum class EstadoMapaReporte(val idEstado: Int, val etiqueta: String) {
    PENDIENTE(1,  "Pendiente"),
    EN_PROCESO(2, "En Proceso"),
    RESUELTO(3,   "Resuelto");

    companion object {
        fun fromId(id: Int) = entries.find { it.idEstado == id } ?: PENDIENTE
        fun fromNombre(nombre: String) =
            entries.find { it.etiqueta.equals(nombre, ignoreCase = true) } ?: PENDIENTE
    }
}

enum class CategoriaIncidencia(val idIncidencia: Int, val etiqueta: String) {
    TODOS(0,      "Todos"),
    BACHE(1,      "Bache"),
    BASURA(2,     "Basura"),
    LUMINARIA(3,  "Luminaria"),
    DRENAJE(4,    "Drenaje"),
    OTRO(5,       "Otro");

    companion object {
        fun fromId(id: Int) = entries.find { it.idIncidencia == id } ?: OTRO
        fun fromNombre(nombre: String) =
            entries.find { it.etiqueta.equals(nombre, ignoreCase = true) } ?: OTRO
    }
}
