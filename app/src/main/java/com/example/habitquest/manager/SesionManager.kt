package com.example.habitquest.manager

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor de sesión usando SharedPreferences
 * Responsable de guardar, recuperar y limpiar datos de sesión
 */
class SesionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)

    companion object {
        private const val NOMBRE_ARCHIVO = "habitquest_sesion"
        private const val CLAVE_USUARIO_ID = "usuario_id"
        private const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        private const val CLAVE_CORREO = "correo_usuario"
        private const val CLAVE_CLASE = "clase_usuario"
    }

    /**
     * Guardar datos de sesión cuando el usuario inicia sesión
     */
    fun guardarSesion(
        usuarioId: Int,
        nombre: String,
        correo: String,
        clase: String
    ) {
        sharedPreferences.edit().apply {
            putInt(CLAVE_USUARIO_ID, usuarioId)
            putString(CLAVE_NOMBRE_USUARIO, nombre)
            putString(CLAVE_CORREO, correo)
            putString(CLAVE_CLASE, clase)
            apply()
        }
    }

    /**
     * Obtener el ID del usuario actual
     */
    fun obtenerUsuarioId(): Int {
        return sharedPreferences.getInt(CLAVE_USUARIO_ID, -1)
    }

    /**
     * Obtener el nombre del usuario actual
     */
    fun obtenerNombreUsuario(): String? {
        return sharedPreferences.getString(CLAVE_NOMBRE_USUARIO, null)
    }

    /**
     * Obtener el correo del usuario actual
     */
    fun obtenerCorreo(): String? {
        return sharedPreferences.getString(CLAVE_CORREO, null)
    }

    /**
     * Obtener la clase del usuario actual
     */
    fun obtenerClase(): String? {
        return sharedPreferences.getString(CLAVE_CLASE, null)
    }

    /**
     * Verificar si hay una sesión activa
     */
    fun haySessionActiva(): Boolean {
        return obtenerUsuarioId() != -1
    }

    /**
     * Cerrar sesión (limpiar datos)
     */
    fun cerrarSesion() {
        sharedPreferences.edit().apply {
            remove(CLAVE_USUARIO_ID)
            remove(CLAVE_NOMBRE_USUARIO)
            remove(CLAVE_CORREO)
            remove(CLAVE_CLASE)
            apply()
        }
    }
}

