package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica del registro de usuario
 *
 * Gestiona la creación de cuenta, validaciones y navegación
 * después del registro exitoso.
 */
class RegistroViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    // Estados para la UI
    private val _estadoRegistro = MutableStateFlow<EstadoRegistro>(EstadoRegistro.Idle)
    val estadoRegistro: StateFlow<EstadoRegistro> = _estadoRegistro

    // Campos del formulario
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo

    private val _contraseña = MutableStateFlow("")
    val contraseña: StateFlow<String> = _contraseña

    private val _clase = MutableStateFlow("Warrior")
    val clase: StateFlow<String> = _clase

    /**
     * Actualizar nombre
     */
    fun actualizarNombre(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    /**
     * Actualizar correo
     */
    fun actualizarCorreo(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
    }

    /**
     * Actualizar contraseña
     */
    fun actualizarContraseña(nuevaContraseña: String) {
        _contraseña.value = nuevaContraseña
    }

    /**
     * Actualizar clase
     */
    fun actualizarClase(nuevaClase: String) {
        _clase.value = nuevaClase
    }

    /**
     * Intentar registrar usuario
     */
    fun registrarUsuario() {
        val nombreActual = _nombre.value.trim()
        val correoActual = _correo.value.trim()
        val contraseñaActual = _contraseña.value
        val claseActual = _clase.value

        // Validaciones básicas
        if (nombreActual.isEmpty()) {
            _estadoRegistro.value = EstadoRegistro.Error("El nombre es obligatorio")
            return
        }

        if (correoActual.isEmpty()) {
            _estadoRegistro.value = EstadoRegistro.Error("El correo es obligatorio")
            return
        }

        if (!correoActual.contains("@")) {
            _estadoRegistro.value = EstadoRegistro.Error("El correo debe ser válido")
            return
        }

        if (contraseñaActual.length < 6) {
            _estadoRegistro.value = EstadoRegistro.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        // Cambiar a estado cargando
        _estadoRegistro.value = EstadoRegistro.Cargando

        // Ejecutar registro en corrutina
        viewModelScope.launch {
            try {
                val idUsuario = usuarioRepository.registrarUsuario(
                    nombre = nombreActual,
                    correo = correoActual,
                    contraseña = contraseñaActual,
                    clase = claseActual
                )

                if (idUsuario > 0) {
                    // Registro exitoso
                    sesionManager.guardarSesion(idUsuario.toInt(), nombreActual, correoActual, claseActual)
                    _estadoRegistro.value = EstadoRegistro.Exitoso
                } else {
                    // Error: correo ya existe
                    _estadoRegistro.value = EstadoRegistro.Error("El correo ya está registrado")
                }
            } catch (e: Exception) {
                // Error inesperado
                _estadoRegistro.value = EstadoRegistro.Error("Error al crear cuenta. Inténtalo de nuevo.")
            }
        }
    }

    /**
     * Resetear estado (útil después de mostrar error)
     */
    fun resetearEstado() {
        _estadoRegistro.value = EstadoRegistro.Idle
    }
}

/**
 * Estados posibles del proceso de registro
 */
sealed class EstadoRegistro {
    object Idle : EstadoRegistro()           // Estado inicial
    object Cargando : EstadoRegistro()       // Creando cuenta
    object Exitoso : EstadoRegistro()        // Registro exitoso
    data class Error(val mensaje: String) : EstadoRegistro() // Error con mensaje
}
