package com.example.habitquest.manager

import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)

    companion object {
        private const val CLAVE_XP = "usuario_xp"
        private const val CLAVE_NIVEL = "usuario_nivel"
        private const val NOMBRE_ARCHIVO = "habitquest_sesion"
        private const val CLAVE_USUARIO_ID = "usuario_id"
        private const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        private const val CLAVE_CORREO = "correo_usuario"
        private const val CLAVE_CLASE = "clase_usuario"
        private const val CLAVE_TOKEN = "jwt_token"           // ← NUEVO: token JWT
        private const val CLAVE_USUARIO_API_ID = "usuario_api_id" // ← NUEVO: id del backend
    }

    // ─── SESIÓN LOCAL (igual que antes) ──────────────────────────

    fun guardarSesion(usuarioId: Int, nombre: String, correo: String, clase: String) {
        sharedPreferences.edit().apply {
            putInt(CLAVE_USUARIO_ID, usuarioId)
            putString(CLAVE_NOMBRE_USUARIO, nombre)
            putString(CLAVE_CORREO, correo)
            putString(CLAVE_CLASE, clase)
            apply()
        }
    }

    fun guardarXP(xp: Int) {
        sharedPreferences.edit().putInt(CLAVE_XP, xp).apply()
    }

    fun obtenerXP(): Int = sharedPreferences.getInt(CLAVE_XP, 0)

    fun guardarNivel(nivel: Int) {
        sharedPreferences.edit().putInt(CLAVE_NIVEL, nivel).apply()
    }

    fun obtenerNivel(): Int = sharedPreferences.getInt(CLAVE_NIVEL, 1)

    fun obtenerUsuarioId(): Int = sharedPreferences.getInt(CLAVE_USUARIO_ID, -1)
    fun obtenerNombreUsuario(): String? = sharedPreferences.getString(CLAVE_NOMBRE_USUARIO, null)
    fun obtenerCorreo(): String? = sharedPreferences.getString(CLAVE_CORREO, null)
    fun obtenerClase(): String? = sharedPreferences.getString(CLAVE_CLASE, null)
    fun haySessionActiva(): Boolean = obtenerUsuarioId() != -1

    // ─── TOKEN JWT (NUEVO - para el backend) ─────────────────────

    fun guardarToken(token: String) {
        sharedPreferences.edit().putString(CLAVE_TOKEN, token).apply()
    }

    fun obtenerToken(): String? = sharedPreferences.getString(CLAVE_TOKEN, null)

    fun hayToken(): Boolean = obtenerToken() != null

    fun guardarUsuarioApiId(id: Int) {
        sharedPreferences.edit().putInt(CLAVE_USUARIO_API_ID, id).apply()
    }

    fun obtenerUsuarioApiId(): Int = sharedPreferences.getInt(CLAVE_USUARIO_API_ID, -1)

    // ─── CERRAR SESIÓN ────────────────────────────────────────────

    fun cerrarSesion() {
        sharedPreferences.edit().clear().apply()
    }
}
