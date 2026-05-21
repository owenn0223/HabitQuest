package com.example.habitquest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el Dashboard con integración de API REST
 */
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()
    private val habitDao = database.habitDao()
    private val sesionManager = SesionManager(application)

    // ESTADOS DE LA API
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // DATOS DEL HÉROE
    private val _nombreHeroe = MutableStateFlow("Héroe")
    val nombreHeroe: StateFlow<String> = _nombreHeroe

    private val _nivel = MutableStateFlow(1)
    val nivel: StateFlow<Int> = _nivel

    private val _xpActual = MutableStateFlow(0)
    val xpActual: StateFlow<Int> = _xpActual

    private val _xpSigNivel = MutableStateFlow(100)
    val xpSigNivel: StateFlow<Int> = _xpSigNivel

    private val _progresoXP = MutableStateFlow(0f)
    val progresoXP: StateFlow<Float> = _progresoXP

    private val _habitosCompletados = MutableStateFlow(0)
    val habitosCompletados: StateFlow<Int> = _habitosCompletados

    private val _totalHabitos = MutableStateFlow(0)
    val totalHabitos: StateFlow<Int> = _totalHabitos

    init {
        cargarDatosIniciales()
        refrescarDesdeApi()
    }

    private fun cargarDatosIniciales() {
        _nombreHeroe.value = sesionManager.obtenerNombreUsuario() ?: "Héroe"
        // Cargar datos locales de Room mientras la API responde
        viewModelScope.launch {
            val userId = sesionManager.obtenerUsuarioId()
            if (userId != -1) {
                val usuario = usuarioDao.getUsuarioById(userId)
                usuario?.let {
                    actualizarEstadoNivel(it.xpTotal)
                }
            }
            _totalHabitos.value = habitDao.getHabitsCount()
            _habitosCompletados.value = habitDao.getCompletedHabitsCount()
        }
    }

    /**
     * Obtiene el progreso real desde el servidor
     */
    fun refrescarDesdeApi() {
        viewModelScope.launch {
            val token = sesionManager.obtenerToken() ?: return@launch
            _isLoading.value = true
            _error.value = null

            try {
                val bearer = "Bearer $token"

                // 1. Obtener progreso (nivel, xp)
                val progressResponse = RetrofitClient.instance.getProgress(bearer)
                if (progressResponse.isSuccessful) {
                    val progress = progressResponse.body()
                    progress?.let {
                        _nivel.value = it.level
                        _xpActual.value = it.xp
                        // Actualizamos localmente el XP en Room para mantener sincronía
                        val userId = sesionManager.obtenerUsuarioId()
                        if (userId != -1) {
                            // Aquí podrías actualizar el XP total en tu BD local
                        }
                    }
                }

                // 2. Obtener estadísticas generales
                val statsResponse = RetrofitClient.instance.getStats(bearer)
                if (statsResponse.isSuccessful) {
                    val stats = statsResponse.body()
                    stats?.let {
                        _totalHabitos.value = it.totalHabits
                        _habitosCompletados.value = it.completedHabits
                    }
                }

            } catch (e: Exception) {
                _error.value = "Modo offline: No se pudo conectar con el servidor"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Lógica local para calcular el progreso de la barra de XP
     */
    private fun actualizarEstadoNivel(xpTotal: Int) {
        var nivelCalculado = 1
        var xpAcumulado = 0
        while (xpTotal >= xpAcumulado + (nivelCalculado * 100)) {
            xpAcumulado += nivelCalculado * 100
            nivelCalculado++
        }

        val xpEnEsteNivel = xpTotal - xpAcumulado
        val xpRequerida = nivelCalculado * 100

        _nivel.value = nivelCalculado
        _xpActual.value = xpEnEsteNivel
        _xpSigNivel.value = xpRequerida
        _progresoXP.value = xpEnEsteNivel.toFloat() / xpRequerida.toFloat()
    }

    fun borrarError() { _error.value = null }
}