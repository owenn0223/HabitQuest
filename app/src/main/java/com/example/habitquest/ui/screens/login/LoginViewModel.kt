package com.example.habitquest.ui.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.LoginRequest
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> get() = _loginSuccess

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val request = LoginRequest(email = email, password = password)
                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.data.token.isNotEmpty()) {
                        // Guardar token
                        sesionManager.guardarToken(body.data.token)

                        // ← NUEVO: Guardar datos del usuario en sesión
                        val user = body.data.user
                        sesionManager.guardarSesion(
                            usuarioId = user.id,
                            nombre = user.name,
                            correo = user.email,
                            clase = user.playerClass ?: "GUERRERO"
                        )
                        sesionManager.guardarUsuarioApiId(user.id)

                        // ← NUEVO: Guardar XP del usuario
                        sesionManager.guardarXP(user.xp)
                        sesionManager.guardarNivel(user.level)

                        _loginSuccess.value = true
                    } else {
                        _error.value = "Respuesta inválida del servidor"
                    }
                } else {
                    _error.value = "Correo o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión a internet"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetLoginState() {
        _loginSuccess.value = false
        _error.value = null
    }
}