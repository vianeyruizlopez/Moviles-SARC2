package com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities

data class NuevoReporte(
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val latitud: Double?,
    val longitud: Double?,
    val idIncidencia: Int,
    val imagenBytes: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NuevoReporte) return false
        return titulo == other.titulo &&
            descripcion == other.descripcion &&
            ubicacion == other.ubicacion &&
            idIncidencia == other.idIncidencia &&
            imagenBytes.contentEquals(other.imagenBytes ?: byteArrayOf())
    }

    override fun hashCode(): Int {
        var result = titulo.hashCode()
        result = 31 * result + descripcion.hashCode()
        result = 31 * result + idIncidencia
        result = 31 * result + (imagenBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class CategoriaIncidencia(
    val id: Int,
    val nombre: String,
    val emoji: String
)
