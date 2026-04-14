package com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens

import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroCategoriaDto
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroEstadoDto
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa

data class MapaCiudadanoUiState(
    val reportes:           List<ReporteMapa>  = emptyList(),
    val isLoading:          Boolean            = false,
    val errorMessage:       String?            = null,
    val filtroCategoria:    FiltroCategoriaDto = FiltroCategoriaDto.TODOS,
    val filtroEstado:       FiltroEstadoDto    = FiltroEstadoDto.TODOS,
    val reporteSeleccionado: ReporteMapa?      = null,  // para el bottom sheet al tocar un marcador
    val mostrarFiltros:     Boolean            = true
)
