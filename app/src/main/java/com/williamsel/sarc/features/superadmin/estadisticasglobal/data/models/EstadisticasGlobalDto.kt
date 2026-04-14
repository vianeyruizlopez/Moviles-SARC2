package com.williamsel.sarc.features.superadmin.estadisticasglobal.data.models

import com.google.gson.annotations.SerializedName

data class EstadisticasGlobalDto(
    @SerializedName("total_reportes")        val totalReportes: Int,
    @SerializedName("ciudadanos")            val ciudadanos: Int,
    @SerializedName("reportes")              val reportes: Int,
    @SerializedName("admins_activos")        val adminsActivos: Int,
    @SerializedName("por_categoria")         val porCategoria: List<CategoriaStatsDto>,
    @SerializedName("por_estado")            val porEstado: List<EstadoStatsDto>,
    @SerializedName("tendencia_30_dias")     val tendencia30Dias: List<TendenciaDiaDto>,
    @SerializedName("total_30_dias")         val total30Dias: Int,
    @SerializedName("promedio_30_dias")      val promedio30Dias: Double,
    @SerializedName("distribucion")          val distribucion: List<DistribucionItemDto>,
    @SerializedName("tiempo_promedio_horas") val tiempoPromedioHoras: Int,
    @SerializedName("tiempos_por_categoria") val tiemposPorCategoria: List<TiempoCategoriaDto>,
    @SerializedName("reportes_activos")      val reportesActivos: Int
)

data class CategoriaStatsDto(
    @SerializedName("nombre")     val nombre: String,
    @SerializedName("cantidad")   val cantidad: Int,
    @SerializedName("porcentaje") val porcentaje: Float
)

data class EstadoStatsDto(
    @SerializedName("nombre")     val nombre: String,
    @SerializedName("cantidad")   val cantidad: Int,
    @SerializedName("porcentaje") val porcentaje: Float
)

data class TendenciaDiaDto(
    @SerializedName("dia")      val dia: Int,
    @SerializedName("cantidad") val cantidad: Int
)

data class DistribucionItemDto(
    @SerializedName("categoria")  val categoria: String,
    @SerializedName("pendiente")  val pendiente: Int,
    @SerializedName("en_proceso") val enProceso: Int,
    @SerializedName("resuelto")   val resuelto: Int
)

data class TiempoCategoriaDto(
    @SerializedName("categoria") val categoria: String,
    @SerializedName("emoji")     val emoji: String,
    @SerializedName("horas")     val horas: Int
)
