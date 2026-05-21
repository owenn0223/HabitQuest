package com.example.habitquest.ui.screens.createhero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.RegisterRequest
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _estadoRegistro = MutableStateFlow<EstadoRegistro>(EstadoRegistro.Idle)
    val estadoRegistro: StateFlow<EstadoRegistro> = _estadoRegistro

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo

    private val _contraseña = MutableStateFlow("")
    val contraseña: StateFlow<String> = _contraseña

    private val _clase = MutableStateFlow("Warrior")
    val clase: StateFlow<String> = _clase

    fun actualizarNombre(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun actualizarCorreo(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
    }

    fun actualizarContraseña(nuevaContraseña: String) {
        _contraseña.value = nuevaContraseña
    }

    fun actualizarClase(nuevaClase: String) {
        _clase.value = nuevaClase
    }

    fun registrarUsuario() {
        val nombreActual = _nombre.value.trim()
        val correoActual = _correo.value.trim()
        val contraseñaActual = _contraseña.value
        val claseActual = _clase.value

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

        _estadoRegistro.value = EstadoRegistro.Cargando

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    name = nombreActual,
                    email = correoActual,
                    password = contraseñaActual,
                    class_ = claseActual
                )

                val response = RetrofitClient.instance.register(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && !body.token.isNullOrEmpty()) {
                        sesionManager.guardarToken(body.token)
                        _estadoRegistro.value = EstadoRegistro.Exitoso
                    } else {
                        _estadoRegistro.value = EstadoRegistro.Error("Respuesta inválida del servidor")
                    }
                } else {
                    _estadoRegistro.value = EstadoRegistro.Error("El correo ya está registrado")
                }
            } catch (e: Exception) {
                _estadoRegistro.value = EstadoRegistro.Error("Sin conexión a internet")
            }
        }
    }

    fun resetearEstado() {
        _estadoRegistro.value = EstadoRegistro.Idle
    }
}

sealed class EstadoRegistro {
    object Idle : EstadoRegistro()
    object Cargando : EstadoRegistro()
    object Exitoso : EstadoRegistro()
    data class Error(val mensaje: String) : EstadoRegistro()
}

