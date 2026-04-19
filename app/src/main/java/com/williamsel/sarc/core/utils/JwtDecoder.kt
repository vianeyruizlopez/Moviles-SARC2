package com.williamsel.sarc.core.utils

import android.util.Base64
import org.json.JSONObject

object JwtDecoder {
    fun decode(token: String): Map<String, Any> {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return emptyMap()
            
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING))
            val jsonObject = JSONObject(payload)
            val map = mutableMapOf<String, Any>()
            
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = jsonObject.get(key)
            }
            map
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun getUserId(token: String): Int {
        val data = decode(token)
        return data["id"] as? Int ?: (data["id"] as? String)?.toIntOrNull() ?: -1
    }

    fun getRolId(token: String): Int {
        val data = decode(token)
        return data["idRol"] as? Int ?: (data["idRol"] as? String)?.toIntOrNull() ?: -1
    }

    fun getRolName(token: String): String {
        return when (getRolId(token)) {
            1 -> "Administrador"
            2 -> "Ciudadano"
            3 -> "SuperAdministrador"
            else -> "Ciudadano"
        }
    }
}
