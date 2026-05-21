package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.ApiAchievement
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Logros (Achievements) conectado a la API
 */
class AchievementsViewModel(
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _achievements = MutableStateFlow<List<ApiAchievement>>(emptyList())
    val achievements: StateFlow<List<ApiAchievement>> = _achievements

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchAchievements()
    }

    fun fetchAchievements() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val token = sesionManager.obtenerToken() ?: return@launch
                val bearer = "Bearer $token"
                val response = RetrofitClient.instance.getAchievements(bearer)
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    _achievements.value = data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión a internet"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
