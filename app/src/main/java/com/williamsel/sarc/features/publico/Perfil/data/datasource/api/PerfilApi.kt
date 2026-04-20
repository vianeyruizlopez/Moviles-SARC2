package com.williamsel.sarc.features.perfil.data.datasource.api

import com.williamsel.sarc.features.perfil.data.models.PerfilDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PerfilApi {
    @GET("usuarios/perfil/{id}")
    suspend fun getPerfil(@Path("id") id: Int): PerfilDto
}