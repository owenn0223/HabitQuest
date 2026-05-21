package com.example.habitquest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.model.Habit
import com.example.habitquest.network.ApiHabit
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel para la pantalla de Lista de Hábitos conectado a la API
 */
class HabitsListViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()
    private val usuarioDao = database.usuarioDao()
    private val sesionManager = com.example.habitquest.manager.SesionManager(application)

    // ESTADOS DE RED (Nuevos para Entrega 3)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de la UI
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private val _currentFilter = MutableStateFlow("All")
    val currentFilter: StateFlow<String> = _currentFilter

    private val _remainingHabits = MutableStateFlow(0)
    val remainingHabits: StateFlow<Int> = _remainingHabits

    private val _userLevel = MutableStateFlow(1)
    val userLevel: StateFlow<Int> = _userLevel

    init {
        // Al iniciar, sincronizamos con la API
        sincronizarConApi()
        // Observamos la base de datos local (Room es la fuente de la UI)
        observarBaseDeDatosLocal()
        loadUserLevel()
    }

    /**
     * SINCRONIZACIÓN CON API (Pattern solicitado)
     */
    fun sincronizarConApi() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val token = sesionManager.obtenerToken()
                if (token == null) {
                    _error.value = "No hay sesión activa"
                    return@launch
                }

                val bearer = "Bearer $token"
                val response = RetrofitClient.instance.getHabits(bearer)

                if (response.isSuccessful) {
                    val apiHabits = response.body() ?: emptyList()

                    // Convertir ApiHabit -> Habit y guardar en Room
                    val localHabits = apiHabits.map { mapApiToLocal(it) }

                    // Limpiar locales y actualizar con los de la API
                    habitDao.deleteAllHabits()
                    localHabits.forEach { habitDao.insertHabit(it) }

                } else {
                    _error.value = "Error del servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión a internet. Mostrando datos locales."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Observa Room y actualiza la UI automáticamente
     */
    private fun observarBaseDeDatosLocal() {
        viewModelScope.launch {
            _currentFilter.collectLatest { filter ->
                val habitsFlow = when (filter) {
                    "All" -> habitDao.getAllHabits()
                    "Daily" -> habitDao.getHabitsByFrecuencia("DAILY")
                    "Weekly" -> habitDao.getHabitsByFrecuencia("WEEKLY")
                    "Monthly" -> habitDao.getHabitsByFrecuencia("MONTHLY")
                    else -> habitDao.getAllHabits()
                }

                habitsFlow.collectLatest { habitList ->
                    _habits.value = habitList
                    _remainingHabits.value = habitList.count { !it.completado }
                }
            }
        }
    }

    /**
     * Mapea el modelo de la API al modelo de Room
     */
    private fun mapApiToLocal(apiHabit: ApiHabit): Habit {
        return Habit(
            id = apiHabit.id,
            nombre = apiHabit.name,
            // El backend usa 'type' (SALUD, etc), lo mapeamos a frecuencia temporalmente
            // o adaptamos según necesites
            frecuencia = when(apiHabit.type) {
                "SALUD", "BIENESTAR" -> "DAILY"
                else -> "WEEKLY"
            },
            dificultad = apiHabit.difficulty,
            xp = when(apiHabit.difficulty) {
                "FACIL" -> 10
                "MEDIA" -> 20
                "DIFICIL" -> 40
                else -> 10
            },
            completado = false, // El estado de completado diario suele ser local o desde otra tabla
            fechaCreacion = apiHabit.createdAt ?: getCurrentDate()
        )
    }

    // --- LÓGICA DE PROGRESO Y NIVELES ---

    private fun loadUserLevel() {
        viewModelScope.launch {
            val userId = sesionManager.obtenerUsuarioId()
            if (userId != -1) {
                usuarioDao.getUsuarioByIdFlow(userId).collectLatest { usuario ->
                    usuario?.let {
                        val nivelInfo = calcularNivel(it.xpTotal)
                        _userLevel.value = nivelInfo.nivel
                    }
                }
            }
        }
    }

    private fun calcularNivel(xpTotal: Int): NivelInfo {
        var nivel = 1
        var xpAcumulado = 0
        while (xpTotal >= xpAcumulado + (nivel * 100)) {
            xpAcumulado += nivel * 100
            nivel++
        }
        return NivelInfo(nivel, xpTotal - xpAcumulado, nivel * 100, 0f)
    }

    private data class NivelInfo(val nivel: Int, val xpEnNivel: Int, val xpParaSiguiente: Int, val porcentaje: Float)

    // --- ACCIONES DEL USUARIO ---

    fun setFilter(filter: String) {
        _currentFilter.value = filter
    }

    fun toggleHabitCompletion(habitId: Int) {
        viewModelScope.launch {
            val habit = habitDao.getHabitById(habitId)
            habit?.let {
                val token = sesionManager.obtenerToken() ?: return@launch

                // 1. Informar a la API
                try {
                    val response = RetrofitClient.instance.completeHabit("Bearer $token", habitId)
                    if (response.isSuccessful) {
                        // 2. Si la API lo acepta, actualizamos localmente
                        val updatedHabit = it.copy(
                            completado = !it.completado,
                            ultimaVezCompletado = getCurrentDate()
                        )
                        habitDao.updateHabit(updatedHabit)

                        // Sumar XP local para feedback inmediato
                        val userId = sesionManager.obtenerUsuarioId()
                        if (userId != -1 && !it.completado) {
                            usuarioDao.sumarXPTotal(userId, it.xp)
                        }
                    }
                } catch (e: Exception) {
                    _error.value = "No se pudo sincronizar el progreso"
                }
            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            val token = sesionManager.obtenerToken() ?: return@launch
            try {
                val response = RetrofitClient.instance.deleteHabit("Bearer $token", habitId)
                if (response.isSuccessful) {
                    habitDao.deleteHabitById(habitId)
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar"
            }
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun clearError() { _error.value = null }
}
