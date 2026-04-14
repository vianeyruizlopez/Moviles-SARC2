package com.williamsel.sarc.features.publico.registro.data.datasource.api

import com.williamsel.sarc.features.publico.registro.data.models.RegistroDto
import com.williamsel.sarc.features.publico.registro.data.models.RegistroRequestDto
import com.williamsel.sarc.features.publico.registro.data.models.RegistroGoogleRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistroApi {
    @POST("auth/register")
    suspend fun registrar(
        @Body body: RegistroRequestDto
    ): RegistroDto
    @POST("auth/google")
    suspend fun registrarConGoogle(
        @Body body: RegistroGoogleRequestDto
    ): RegistroDto
}
