package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.RegisterRequest
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar el registro de usuario conectado a la API REST
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

    private val _clase = MutableStateFlow("GUERRERO")
    val clase: StateFlow<String> = _clase

    fun actualizarNombre(nuevoNombre: String) { _nombre.value = nuevoNombre }
    fun actualizarCorreo(nuevoCorreo: String) { _correo.value = nuevoCorreo }
    fun actualizarContraseña(nuevaContraseña: String) { _contraseña.value = nuevaContraseña }
    fun actualizarClase(nuevaClase: String) { _clase.value = nuevaClase }

    /**
     * Registra al usuario en la API y sincroniza con Room
     */
    fun registrarUsuario() {
        val nombreActual = _nombre.value.trim()
        val correoActual = _correo.value.trim()
        val contraseñaActual = _contraseña.value
        val claseActual = _clase.value

        // Validaciones básicas
        if (nombreActual.isEmpty() || correoActual.isEmpty() || contraseñaActual.isEmpty()) {
            _estadoRegistro.value = EstadoRegistro.Error("Todos los campos son obligatorios")
            return
        }

        if (!correoActual.contains("@")) {
            _estadoRegistro.value = EstadoRegistro.Error("Correo inválido")
            return
        }

        _estadoRegistro.value = EstadoRegistro.Cargando

        viewModelScope.launch {
            try {
                // 1. Llamada a la API
                val registerRequest = RegisterRequest(
                    name = nombreActual,
                    email = correoActual,
                    password = contraseñaActual,
                    playerClass = claseActual.uppercase()
                )

                val response = RetrofitClient.instance.register(registerRequest)

                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!

                    // 2. Guardar Token y ID de API
                    sesionManager.guardarToken(authData.token)
                    sesionManager.guardarUsuarioApiId(authData.user.id)

                    // 3. Persistencia local en Room (Fallback)
                    val idLocal = usuarioRepository.registrarUsuario(
                        nombre = nombreActual,
                        correo = correoActual,
                        contraseña = contraseñaActual,
                        clase = claseActual
                    )

                    // 4. Establecer sesión activa
                    sesionManager.guardarSesion(
                        usuarioId = if (idLocal > 0) idLocal.toInt() else authData.user.id,
                        nombre = authData.user.name,
                        correo = authData.user.email,
                        clase = authData.user.playerClass ?: claseActual
                    )

                    _estadoRegistro.value = EstadoRegistro.Exitoso
                } else {
                    val errorMsg = if (response.code() == 400) "El correo ya existe" else "Error: ${response.code()}"
                    _estadoRegistro.value = EstadoRegistro.Error(errorMsg)
                }
            } catch (e: Exception) {
                // Si falla la red, intentamos registro puramente local
                intentarRegistroLocal(nombreActual, correoActual, contraseñaActual, claseActual)
            }
        }
    }

    private suspend fun intentarRegistroLocal(nom: String, cor: String, con: String, cla: String) {
        try {
            val id = usuarioRepository.registrarUsuario(nom, cor, con, cla)
            if (id > 0) {
                sesionManager.guardarSesion(id.toInt(), nom, cor, cla)
                _estadoRegistro.value = EstadoRegistro.Exitoso
            } else {
                _estadoRegistro.value = EstadoRegistro.Error("El correo ya existe localmente")
            }
        } catch (e: Exception) {
            _estadoRegistro.value = EstadoRegistro.Error("Error de conexión al servidor")
        }
    }

    fun resetearEstado() { _estadoRegistro.value = EstadoRegistro.Idle }
}

sealed class EstadoRegistro {
    object Idle : EstadoRegistro()
    object Cargando : EstadoRegistro()
    object Exitoso : EstadoRegistro()
    data class Error(val mensaje: String) : EstadoRegistro()
}
