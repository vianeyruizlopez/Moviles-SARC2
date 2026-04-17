package com.williamsel.sarc.features.ciudadano.registro.data.datasource.api

import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroDto
import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroRequestDto
import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroGoogleRequestDto
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
/*
POST http://localhost:8080/auth/register
Envía: Datos del ciudadano (nombre, email, teléfono).
{
  "nombre": "Admin Maestro",
  "primerApellido": "Lopez",
  "segundoApellido": "García",
  "email": "master@tuapp.com",
  "contrasena": "admin123",
  "edad": 25,
  "idRol": 2
}
 */