package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.LoginRequest
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica del login con integración de API REST
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

    fun actualizarCorreo(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
    }

    fun actualizarContraseña(nuevaContraseña: String) {
        _contraseña.value = nuevaContraseña
    }

    /**
     * Intenta iniciar sesión primero mediante la API y luego localmente
     */
    fun iniciarSesion() {
        val correoActual = _correo.value.trim()
        val contraseñaActual = _contraseña.value

        if (correoActual.isEmpty() || contraseñaActual.isEmpty()) {
            _estadoLogin.value = EstadoLogin.Error("Todos los campos son obligatorios")
            return
        }

        _estadoLogin.value = EstadoLogin.Cargando

        viewModelScope.launch {
            try {
                // 1. Intentar Login con la API
                val loginRequest = LoginRequest(email = correoActual, password = contraseñaActual)
                val response = RetrofitClient.instance.login(loginRequest)

                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!

                    // Guardar Token JWT
                    sesionManager.guardarToken(authData.token)

                    // Guardar sesión del usuario (Backend ID)
                    sesionManager.guardarUsuarioApiId(authData.user.id)
                    sesionManager.guardarSesion(
                        usuarioId = authData.user.id, // Usamos el ID del backend
                        nombre = authData.user.name,
                        correo = authData.user.email,
                        clase = authData.user.playerClass ?: "GUERRERO"
                    )

                    _estadoLogin.value = EstadoLogin.Exitoso
                } else {
                    // Si la API falla (ej: 401 Unauthorized), intentamos fallback local con Room
                    intentarLoginLocal(correoActual, contraseñaActual, "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                // Error de red (sin conexión): fallback a Room para permitir modo offline
                intentarLoginLocal(correoActual, contraseñaActual, "Sin conexión al servidor")
            }
        }
    }

    /**
     * Lógica de respaldo: busca el usuario en la base de datos local (Room)
     */
    private suspend fun intentarLoginLocal(correo: String, pass: String, mensajeErrorOriginal: String) {
        try {
            val usuarioLocal = usuarioRepository.iniciarSesion(correo, pass)
            if (usuarioLocal != null) {
                sesionManager.guardarSesion(
                    usuarioId = usuarioLocal.id,
                    nombre = usuarioLocal.nombre,
                    correo = usuarioLocal.correo,
                    clase = usuarioLocal.clase
                )
                _estadoLogin.value = EstadoLogin.Exitoso
            } else {
                _estadoLogin.value = EstadoLogin.Error(mensajeErrorOriginal)
            }
        } catch (e: Exception) {
            _estadoLogin.value = EstadoLogin.Error("Error de autenticación: $mensajeErrorOriginal")
        }
    }

    fun resetearEstado() {
        _estadoLogin.value = EstadoLogin.Idle
    }
}

/**
 * Estados del proceso de login
 */
sealed class EstadoLogin {
    object Idle : EstadoLogin()
    object Cargando : EstadoLogin()
    object Exitoso : EstadoLogin()
    data class Error(val mensaje: String) : EstadoLogin()
}
