package com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens

import android.graphics.Bitmap
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia

data class CrearReportesUiState(

    val titulo: String                          = "",
    val descripcion: String                     = "",
    val ubicacion: String                       = "",
    val latitud: Double?                        = null,
    val longitud: Double?                       = null,
    val categoriaSeleccionada: CategoriaIncidencia? = null,
    val imagen: Bitmap?                         = null,

    val categorias: List<CategoriaIncidencia>   = emptyList(),

    val isLoading: Boolean                      = false,
    val isCargandoCategorias: Boolean           = false,
    val isEnviado: Boolean                      = false,
    val errorMessage: String?                   = null,

    val errorTitulo: String?                    = null,
    val errorDescripcion: String?               = null,
    val errorCategoria: String?                 = null,
    val errorUbicacion: String?                 = null
)