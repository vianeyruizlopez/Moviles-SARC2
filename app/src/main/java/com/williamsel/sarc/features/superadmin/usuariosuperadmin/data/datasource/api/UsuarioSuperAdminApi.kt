package com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.datasource.api

import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.models.UsuarioSistemaDto
import retrofit2.http.*

interface UsuarioSuperAdminApi {

    /** Staff (admins) */
    @GET("usuarios/staff")
    suspend fun getStaff(): List<UsuarioSistemaDto>

    /** Ciudadanos */
    @GET("usuarios/ciudadanos")
    suspend fun getCiudadanos(): List<UsuarioSistemaDto>

    /** SuperAdmins */
    @GET("usuarios/superadmi")
    suspend fun getSuperAdmins(): List<UsuarioSistemaDto>

    @DELETE("usuarios/{idUsuario}")
    suspend fun eliminarUsuario(
        @Path("idUsuario") idUsuario: Int
    ): retrofit2.Response<Unit>
}
