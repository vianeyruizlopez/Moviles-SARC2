package com.williamsel.sarc.features.superadmin.panelsuperadmin.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.datasource.api.PanelSuperAdminApi
import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.mapper.toDomain
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.entities.PanelSuperAdmin
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.repositories.PanelSuperAdminRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PanelSuperAdminRepositoryImpl @Inject constructor(
    private val api: PanelSuperAdminApi,
    private val reporteDao: ReporteDao,
    private val usuarioDao: UsuarioDao
) : PanelSuperAdminRepository {

    override suspend fun getPanelData(): PanelSuperAdmin {
        return try {
            api.getPanelData().toDomain()
        } catch (e: Exception) {
            // Fallback Room
            val reportes  = reporteDao.getAll().first()
            val usuarios  = usuarioDao.getAll().first()
            PanelSuperAdmin(
                nombreCompleto  = "Super Administrador",
                totalReportes   = reportes.size,
                pendientes      = reportes.count { it.idEstado == 1 },
                enProceso       = reportes.count { it.idEstado == 2 },
                resueltos       = reportes.count { it.idEstado == 3 },
                totalAdmins     = usuarios.count { it.idRol == 2 },
                totalCiudadanos = usuarios.count { it.idRol == 1 }
            )
        }
    }
}
