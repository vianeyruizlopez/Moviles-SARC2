package com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens

import androidx.compose.ui.graphics.Color

data class ReporteMapaUiModel(
    val idReporte: Int,
    val nombre: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val coordenadasTexto: String,
    val fechaTexto: String?,
    val categoriaLabel: String,
    val categoriaColor: Color,
    val marcadorColor: Float,
    val estadoLabel: String,
    val estadoColor: Color
)
