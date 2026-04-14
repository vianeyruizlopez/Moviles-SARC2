package com.williamsel.sarc.features.superadmin.crearusuario.data.datasource.api

import com.williamsel.sarc.features.superadmin.crearusuario.data.models.AdministradorDto
import com.williamsel.sarc.features.superadmin.crearusuario.data.models.CrearAdminRequest
import com.williamsel.sarc.features.superadmin.crearusuario.data.models.MensajeResponse
import retrofit2.http.*

interface CrearUsuarioApi {

    @POST("usuarios/admin")
    suspend fun crearAdministrador(
        @Body request: CrearAdminRequest
    ): MensajeResponse

    @GET("usuarios/staff")
    suspend fun getAdministradores(): List<AdministradorDto>

    @DELETE("usuarios/{idUsuario}")
    suspend fun eliminarAdministrador(
        @Path("idUsuario") idUsuario: Int
    ): retrofit2.Response<Unit>
}
