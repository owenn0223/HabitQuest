package com.example.habitquest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()

    // Hábitos completados hoy vs total (ej: "4/8")
    private val _habitsToday = MutableStateFlow("0/0")
    val habitsToday: StateFlow<String> = _habitsToday

    // Racha actual (días consecutivos con todos los hábitos completados)
    // Simplificado: muestra días con al menos 1 hábito completado
    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    // XP total acumulado (suma de XP de hábitos completados)
    private val _totalXP = MutableStateFlow(0)
    val totalXP: StateFlow<Int> = _totalXP

    // Info de nivel calculada desde XP total
    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    private val _xpInLevel = MutableStateFlow(0)
    val xpInLevel: StateFlow<Int> = _xpInLevel

    private val _xpForNextLevel = MutableStateFlow(100)
    val xpForNextLevel: StateFlow<Int> = _xpForNextLevel

    private val _xpProgress = MutableStateFlow(0f)
    val xpProgress: StateFlow<Float> = _xpProgress

    // Próximo hábito pendiente del día (Current Quest)
    private val _currentQuest = MutableStateFlow<Habit?>(null)
    val currentQuest: StateFlow<Habit?> = _currentQuest

    init {
        observeHabits()
    }

    private fun observeHabits() {
        viewModelScope.launch {
            habitDao.getAllHabits().collectLatest { habits ->
                updateStats(habits)
            }
        }
        viewModelScope.launch {
            habitDao.getIncompleteHabits().collectLatest { pending ->
                // Current Quest = primer hábito pendiente
                _currentQuest.value = pending.firstOrNull()
            }
        }
    }

    private fun updateStats(habits: List<Habit>) {
        val total = habits.size
        val completed = habits.count { it.completado }

        // Hábitos hoy
        _habitsToday.value = "$completed/$total"

        // XP total = suma del XP de todos los hábitos completados
        val xp = habits.filter { it.completado }.sumOf { it.xp }
        _totalXP.value = xp

        // Calcular nivel desde XP
        val nivelInfo = calcularNivel(xp)
        _level.value = nivelInfo.nivel
        _xpInLevel.value = nivelInfo.xpEnNivel
        _xpForNextLevel.value = nivelInfo.xpParaSiguiente
        _xpProgress.value = nivelInfo.porcentaje

        // Racha simplificada: si hoy hay al menos 1 hábito completado = racha activa
        // (una racha real requeriría historial de días anteriores con ProgressoDiario)
        _streak.value = if (completed > 0) 1 else 0
    }

    /**
     * Marcar el Current Quest como completado
     */
    fun completeCurrentQuest() {
        viewModelScope.launch {
            val quest = _currentQuest.value ?: return@launch
            val today = getCurrentDate()
            val updated = quest.copy(
                completado = true,
                ultimaVezCompletado = today
            )
            habitDao.updateHabit(updated)
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Calcula el nivel y progreso de XP
     * Fórmula: XP para nivel N = N * 100
     */
    private fun calcularNivel(xpTotal: Int): NivelInfo {
        var nivel = 1
        var xpAcumulado = 0
        while (xpTotal >= xpAcumulado + (nivel * 100)) {
            xpAcumulado += nivel * 100
            nivel++
        }
        val xpEnNivel = xpTotal - xpAcumulado
        val xpParaSiguiente = nivel * 100
        val porcentaje = if (xpParaSiguiente > 0)
            xpEnNivel.toFloat() / xpParaSiguiente else 0f
        return NivelInfo(nivel, xpEnNivel, xpParaSiguiente, porcentaje)
    }

    private data class NivelInfo(
        val nivel: Int,
        val xpEnNivel: Int,
        val xpParaSiguiente: Int,
        val porcentaje: Float
    )
}