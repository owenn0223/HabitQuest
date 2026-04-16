package com.example.habitquest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habitquest.model.Usuario

/**
 * Data Access Object (DAO) para la entidad Usuario
 *
 * Este DAO define las operaciones CRUD para usuarios,
 * enfocándose en login y registro.
 */

@Dao
interface UsuarioDao {

    /**
     * INSERTAR USUARIO (CREATE)
     *
     * Inserta un nuevo usuario en la base de datos
     * Si hay conflicto (mismo ID), reemplaza
     *
     * @param usuario El usuario a insertar
     * @return Long El ID generado
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario): Long

    /**
     * BUSCAR USUARIO POR CORREO Y CONTRASEÑA (LOGIN)
     *
     * Busca un usuario que coincida con correo y contraseña
     * Retorna null si no existe
     *
     * @param correo Correo del usuario
     * @param contraseña Contraseña del usuario
     * @return Usuario? El usuario encontrado o null
     */
    @Query("SELECT * FROM usuario WHERE correo = :correo AND contraseña = :contraseña LIMIT 1")
    suspend fun getUsuarioByCorreoYContraseña(correo: String, contraseña: String): Usuario?

    /**
     * VERIFICAR SI CORREO YA EXISTE (REGISTRO)
     *
     * Verifica si un correo ya está registrado
     *
     * @param correo Correo a verificar
     * @return Boolean true si existe, false si no
     */
    @Query("SELECT COUNT(*) > 0 FROM usuario WHERE correo = :correo")
    suspend fun existeCorreo(correo: String): Boolean

    /**
     * OBTENER USUARIO POR ID
     *
     * Obtiene un usuario por su ID
     *
     * @param id ID del usuario
     * @return Usuario? El usuario o null si no existe
     */
    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    suspend fun getUsuarioById(id: Int): Usuario?

    /**
     * ACTUALIZAR USUARIO (UPDATE)
     *
     * Actualiza los datos de un usuario existente
     * (Room usa el ID para identificar qué actualizar)
     */
    @androidx.room.Update
    suspend fun updateUsuario(usuario: Usuario)

    /**
     * ELIMINAR USUARIO (DELETE)
     *
     * Elimina un usuario de la base de datos
     */
    @androidx.room.Delete
    suspend fun deleteUsuario(usuario: Usuario)
}
