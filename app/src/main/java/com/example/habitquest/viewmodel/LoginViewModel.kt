package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica del login
 *
 * Gestiona la validación de credenciales, manejo de errores
 * y navegación después del login exitoso.
 */
class LoginViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    // Estados para la UI
    private val _estadoLogin = MutableStateFlow<EstadoLogin>(EstadoLogin.Idle)
    val estadoLogin: StateFlow<EstadoLogin> = _estadoLogin

    // Campos del formulario
    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo

    private val _contraseña = MutableStateFlow("")
    val contraseña: StateFlow<String> = _contraseña

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
     * Intentar iniciar sesión
     */
    fun iniciarSesion() {
        val correoActual = _correo.value.trim()
        val contraseñaActual = _contraseña.value

        // Validaciones básicas
        if (correoActual.isEmpty()) {
            _estadoLogin.value = EstadoLogin.Error("El correo es obligatorio")
            return
        }

        if (contraseñaActual.isEmpty()) {
            _estadoLogin.value = EstadoLogin.Error("La contraseña es obligatoria")
            return
        }

        // Cambiar a estado cargando
        _estadoLogin.value = EstadoLogin.Cargando

        // Ejecutar login en corrutina
        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.iniciarSesion(correoActual, contraseñaActual)

                if (usuario != null) {
                    // Login exitoso: guardar sesión
                    sesionManager.guardarSesion(
                        usuarioId = usuario.id,
                        nombre = usuario.nombre,
                        correo = usuario.correo,
                        clase = usuario.clase
                    )

                    _estadoLogin.value = EstadoLogin.Exitoso
                } else {
                    // Credenciales inválidas
                    _estadoLogin.value = EstadoLogin.Error("Correo o contraseña incorrectos")
                }
            } catch (e: Exception) {
                // Error inesperado
                _estadoLogin.value = EstadoLogin.Error("Error al iniciar sesión. Inténtalo de nuevo.")
            }
        }
    }

    /**
     * Resetear estado (útil después de mostrar error)
     */
    fun resetearEstado() {
        _estadoLogin.value = EstadoLogin.Idle
    }
}

/**
 * Estados posibles del proceso de login
 */
sealed class EstadoLogin {
    object Idle : EstadoLogin()           // Estado inicial
    object Cargando : EstadoLogin()       // Validando credenciales
    object Exitoso : EstadoLogin()        // Login exitoso
    data class Error(val mensaje: String) : EstadoLogin() // Error con mensaje
}
