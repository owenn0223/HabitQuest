package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.manager.SesionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica del splash screen
 * Verifica si hay una sesión activa y determina a qué pantalla navegar
 */
class SplashViewModel(
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _destinoNavegacion = MutableStateFlow<PantallaDestino?>(null)
    val destinoNavegacion: StateFlow<PantallaDestino?> = _destinoNavegacion

    init {
        verificarSesion()
    }

    /**
     * Verificar si hay sesión activa y establecer el destino
     */
    private fun verificarSesion() {
        viewModelScope.launch {
            // Simular pequeño delay para que se vea la animación del splash
            delay(1000)

            val haySession = sesionManager.haySessionActiva()

            _destinoNavegacion.value = if (haySession) {
                PantallaDestino.HOME
            } else {
                PantallaDestino.LOGIN
            }
        }
    }
}

/**
 * Enumeración de posibles destinos después del splash
 */
enum class PantallaDestino {
    HOME,      // El usuario ya tiene sesión
    LOGIN      // El usuario debe iniciar sesión o registrarse
}


