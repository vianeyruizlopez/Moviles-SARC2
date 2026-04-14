package com.williamsel.sarc.features.superadmin.panelsuperadmin.data.datasource.api

import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.models.PanelSuperAdminDto
import retrofit2.http.GET

interface PanelSuperAdminApi {

    @GET("usuarios/estadisticas/conteo")
    suspend fun getPanelData(): PanelSuperAdminDto
}
