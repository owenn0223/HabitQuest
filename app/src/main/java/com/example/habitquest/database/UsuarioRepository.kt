package com.example.habitquest.database

import com.example.habitquest.model.Usuario

/**
 * Repositorio para operaciones con usuarios
 *
 * Esta clase actúa como intermediario entre el ViewModel y la base de datos,
 * encapsulando la lógica de acceso a datos y proporcionando una API limpia.
 */
class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * REGISTRAR NUEVO USUARIO
     *
     * Inserta un nuevo usuario en la base de datos
     * Retorna el ID generado o -1 si falla
     */
    suspend fun registrarUsuario(
        nombre: String,
        correo: String,
        contraseña: String,
        clase: String
    ): Long {
        // Verificar si el correo ya existe
        if (existeCorreo(correo)) {
            return -1L // Error: correo ya registrado
        }

        // Crear usuario con valores por defecto
        val usuario = Usuario(
            nombre = nombre,
            correo = correo,
            contraseña = contraseña,
            clase = clase
        )

        // Insertar en BD
        return usuarioDao.insertUsuario(usuario)
    }

    /**
     * INICIAR SESIÓN
     *
     * Verifica las credenciales y retorna el usuario si son correctas
     * Retorna null si las credenciales son inválidas
     */
    suspend fun iniciarSesion(correo: String, contraseña: String): Usuario? {
        return usuarioDao.getUsuarioByCorreoYContraseña(correo, contraseña)
    }

    /**
     * VERIFICAR SI CORREO EXISTE
     *
     * Útil para validaciones en registro
     */
    suspend fun existeCorreo(correo: String): Boolean {
        return usuarioDao.existeCorreo(correo)
    }

    /**
     * OBTENER USUARIO POR ID
     *
     * Útil para cargar datos del usuario logueado
     */
    suspend fun getUsuarioById(id: Int): Usuario? {
        return usuarioDao.getUsuarioById(id)
    }

    /**
     * ACTUALIZAR USUARIO
     *
     * Para actualizar datos del usuario (ej: nivel, XP, etc.)
     */
    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.updateUsuario(usuario)
    }

    /**
     * ELIMINAR USUARIO
     *
     * Para eliminar cuenta (opcional)
     */
    suspend fun eliminarUsuario(usuario: Usuario) {
        usuarioDao.deleteUsuario(usuario)
    }
}
