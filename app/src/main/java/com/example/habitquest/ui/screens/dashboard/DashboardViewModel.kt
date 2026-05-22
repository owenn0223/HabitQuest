package com.example.habitquest.ui.screens.dashboard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.manager.SesionManager
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
    private val usuarioDao = database.usuarioDao()

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("habitquest_prefs", Context.MODE_PRIVATE)

    // SesionManager para obtener datos del usuario
    private val sesionManager = SesionManager(application)

    companion object {
        private const val LAST_RESET_DATE_KEY = "last_reset_date"
    }

    // Hábitos completados hoy vs total (ej: "4/8")
    private val _habitsToday = MutableStateFlow("0/0")
    val habitsToday: StateFlow<String> = _habitsToday

    // Racha actual (días consecutivos con todos los hábitos completados)
    // Simplificado: muestra días con al menos 1 hábito completado
    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    // XP total acumulado (obtenido del usuario)
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

    // Estadísticas adicionales para StatisticsScreen
    private val _habitsCreated = MutableStateFlow(0)
    val habitsCreated: StateFlow<Int> = _habitsCreated

    private val _habitsCompleted = MutableStateFlow(0)
    val habitsCompleted: StateFlow<Int> = _habitsCompleted

    private val _achievements = MutableStateFlow(0)
    val achievements: StateFlow<Int> = _achievements

    private val _bestStreak = MutableStateFlow(0)
    val bestStreak: StateFlow<Int> = _bestStreak

    // Datos del usuario desde SesionManager
    private val _userName = MutableStateFlow("Hero")
    val userName: StateFlow<String> = _userName

    private val _userClass = MutableStateFlow("WARRIOR")
    val userClass: StateFlow<String> = _userClass

    init {
        loadUserData()
        observeHabits()
    }

    private fun observeHabits() {
        // Reset diario de hábitos
        viewModelScope.launch {
            val today = getCurrentDate()
            val lastReset = sharedPreferences.getString(LAST_RESET_DATE_KEY, "")

            if (today != lastReset) {
                sharedPreferences.edit().putString(LAST_RESET_DATE_KEY, today).apply()
                habitDao.resetAllHabitsCompletion()
            }
        }

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

        // Obtener XP total del usuario (no solo de hoy)
        viewModelScope.launch {
            val usuario = usuarioDao.getFirstUsuario()
            if (usuario != null) {
                val xp = usuario.xpTotal
                _totalXP.value = xp

                // Calcular nivel desde XP
                val nivelInfo = calcularNivel(xp)
                _level.value = nivelInfo.nivel
                _xpInLevel.value = nivelInfo.xpEnNivel
                _xpForNextLevel.value = nivelInfo.xpParaSiguiente
                _xpProgress.value = nivelInfo.porcentaje
            }
        }

        // Racha simplificada: si hoy hay al menos 1 hábito completado = racha activa
        _streak.value = if (completed > 0) 1 else 0

        // Estadísticas adicionales
        _habitsCreated.value = total
        _habitsCompleted.value = completed
        _achievements.value = 0 // TODO: Calcular logros
        _bestStreak.value = 0 // TODO: Calcular mejor racha
    }

    /**
     * Marcar el Current Quest como completado
     */
    private fun actualizarXPUI(xpTotal: Int) {
        _totalXP.value = xpTotal
        val nivelInfo = calcularNivel(xpTotal)
        _level.value = nivelInfo.nivel
        _xpInLevel.value = nivelInfo.xpEnNivel
        _xpForNextLevel.value = nivelInfo.xpParaSiguiente
        _xpProgress.value = nivelInfo.porcentaje
        // Guardar en sesión para persistir
        sesionManager.guardarXP(xpTotal)
        sesionManager.guardarNivel(nivelInfo.nivel)
    }

    fun completeCurrentQuest() {
        viewModelScope.launch {
            val quest = _currentQuest.value ?: return@launch
            val today = getCurrentDate()

            // XP según frecuencia
            val xpGanado = when (quest.frecuencia.uppercase()) {
                "WEEKLY"  -> quest.xp * 2
                "MONTHLY" -> quest.xp * 4
                else      -> quest.xp
            }

            // Marcar hábito completado
            habitDao.updateHabit(quest.copy(
                completado = true,
                ultimaVezCompletado = today
            ))

            // Calcular nuevo XP y actualizar UI inmediatamente
            val xpNuevo = _totalXP.value + xpGanado
            actualizarXPUI(xpNuevo)

            // Guardar en Room
            val usuario = usuarioDao.getFirstUsuario()
            if (usuario != null) {
                usuarioDao.sumarXPTotal(usuario.id, xpGanado)
            }
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

    private fun loadUserData() {
        _userName.value = sesionManager.obtenerNombreUsuario() ?: "Hero"
        _userClass.value = sesionManager.obtenerClase() ?: "GUERRERO"

        // Cargar XP inicial desde sesión
        val xpInicial = sesionManager.obtenerXP()
        _totalXP.value = xpInicial
        val nivelInfo = calcularNivel(xpInicial)
        _level.value = nivelInfo.nivel
        _xpInLevel.value = nivelInfo.xpEnNivel
        _xpForNextLevel.value = nivelInfo.xpParaSiguiente
        _xpProgress.value = nivelInfo.porcentaje
    }
}