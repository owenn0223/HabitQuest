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
import kotlinx.coroutines.flow.first
/**
 * ViewModel para la pantalla de Lista de Hábitos
 *
 * Este ViewModel maneja:
 * - Obtener hábitos de la base de datos
 * - Filtrar hábitos por frecuencia
 * - Marcar hábitos como completados
 * - Eliminar hábitos
 * - Actualizar la UI automáticamente con Flow
 *
 * CARACTERÍSTICAS ACADÉMICAS:
 * - AndroidViewModel: Para acceso a Application context
 * - StateFlow: Para estado observable
 * - viewModelScope: Para corrutinas que sobreviven a cambios de configuración
 * - Flow: Para observabilidad reactiva
 */

class HabitsListViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de la base de datos
    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()

    // Estado de la UI
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    // Filtro actual
    private val _currentFilter = MutableStateFlow("All")
    val currentFilter: StateFlow<String> = _currentFilter

    // Conteo de hábitos restantes (no completados)
    private val _remainingHabits = MutableStateFlow(0)
    val remainingHabits: StateFlow<Int> = _remainingHabits

    init {
        // Cargar datos iniciales
        loadHabits()
    }

    /**
     * CARGA HÁBITOS DINÁMICOS
     *
     * Carga hábitos según el filtro actual
     * Se ejecuta automáticamente cuando cambia el filtro
     */
    private fun loadHabits() {
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
                    // Actualizar conteo de hábitos restantes
                    _remainingHabits.value = habitList.count { !it.completado }
                }
            }
        }
    }

    /**
     * CAMBIAR FILTRO
     *
     * Actualiza el filtro actual y recarga los hábitos
     *
     * @param filter Nuevo filtro ("All", "Daily", "Weekly", "Monthly")
     */
    fun setFilter(filter: String) {
        _currentFilter.value = filter
    }

    /**
     * MARCAR HÁBITO COMO COMPLETADO
     *
     * Alterna el estado de completado de un hábito
     * Actualiza la fecha de última vez completado
     *
     * @param habitId ID del hábito a completar
     */
    fun toggleHabitCompletion(habitId: Int) {
        viewModelScope.launch {
            val habit = habitDao.getHabitById(habitId)
            habit?.let {
                val today = getCurrentDate()
                val updatedHabit = it.copy(
                    completado = !it.completado,
                    ultimaVezCompletado = if (!it.completado) today else it.ultimaVezCompletado
                )
                habitDao.updateHabit(updatedHabit)
            }
        }
    }

    /**
     * ELIMINAR HÁBITO
     *
     * Elimina un hábito de la base de datos
     *
     * @param habitId ID del hábito a eliminar
     */
    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitDao.deleteHabitById(habitId)
        }
    }

    /**
     * RESET DIARIO
     *
     * Resetea el estado de completado de todos los hábitos
     * Útil para iniciar un nuevo día
     */
    fun resetDailyHabits() {
        viewModelScope.launch {
            habitDao.resetAllHabitsCompletion()
        }
    }

    /**
     * AGREGAR HÁBITO DE EJEMPLO
     *
     * Método temporal para testing - agrega hábitos de ejemplo
     * En producción, esto se haría desde CreateHabitScreen
     */
    fun addSampleHabits() {
        viewModelScope.launch {
            val sampleHabits = listOf(
                Habit(
                    nombre = "Morning Meditation",
                    frecuencia = "DAILY",
                    dificultad = "EASY",
                    xp = 10,
                    fechaCreacion = getCurrentDate()
                ),
                Habit(
                    nombre = "Heavy Lifting Session",
                    frecuencia = "WEEKLY",
                    dificultad = "HARD",
                    xp = 40,
                    fechaCreacion = getCurrentDate()
                ),
                Habit(
                    nombre = "Drink 2L Water",
                    frecuencia = "DAILY",
                    dificultad = "EASY",
                    xp = 10,
                    fechaCreacion = getCurrentDate()
                ),
                Habit(
                    nombre = "Read 20 Pages",
                    frecuencia = "DAILY",
                    dificultad = "MED",
                    xp = 20,
                    fechaCreacion = getCurrentDate()
                )
            )

            sampleHabits.forEach { habit ->
                habitDao.insertHabit(habit)
            }
        }
    }

    /**
     * OBTENER FECHA ACTUAL
     *
     * Utilidad para formatear fechas
     *
     * @return Fecha actual en formato "yyyy-MM-dd"
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * CONTAR HÁBITOS POR FILTRO
     *
     * Método auxiliar para estadísticas
     *
     * @param filter Filtro a contar
     * @return Número de hábitos en ese filtro
     */
    suspend fun getHabitsCountByFilter(filter: String): Int {
        return when (filter) {
            "All" -> habitDao.getHabitsCount()
            "Daily" -> habitDao.getHabitsByFrecuencia("DAILY").first().size
            "Weekly" -> habitDao.getHabitsByFrecuencia("WEEKLY").first().size
            "Monthly" -> habitDao.getHabitsByFrecuencia("MONTHLY").first().size
            else -> 0
        }
    }
}

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿POR QUÉ ESTE VIEWMODEL?
 *
 * 1. SEPARACIÓN DE RESPONSABILIDADES:
 *    - ViewModel: Maneja estado y lógica de UI
 *    - DAO: Maneja operaciones de BD
 *    - UI: Solo muestra datos
 *
 * 2. OBSERVABILIDAD CON FLOW:
 *    - _habits es MutableStateFlow (privado para modificar)
 *    - habits es StateFlow (público para observar)
 *    - UI se actualiza automáticamente cuando cambian los datos
 *
 * 3. FILTROS DINÁMICOS:
 *    - _currentFilter controla qué hábitos mostrar
 *    - loadHabits() se ejecuta cada vez que cambia el filtro
 *    - collectLatest asegura que solo procesa el último valor
 *
 * 4. OPERACIONES CRUD:
 *    - toggleHabitCompletion(): Update
 *    - deleteHabit(): Delete
 *    - addSampleHabits(): Create (temporal)
 *
 * 5. CORRUTINAS:
 *    - viewModelScope.launch: Para operaciones asíncronas
 *    - Sobreviven a cambios de configuración
 *    - Se cancelan automáticamente cuando ViewModel se destruye
 *
 * ---
 *
 * CONEXIÓN CON LA UI:
 *
 * En HabitsListScreen.kt:
 *
 * val viewModel: HabitsListViewModel = viewModel()
 * val habits by viewModel.habits.collectAsState()
 * val currentFilter by viewModel.currentFilter.collectAsState()
 * val remainingHabits by viewModel.remainingHabits.collectAsState()
 *
 * // En LazyColumn:
 * items(habits) { habit ->
 *     HabitCard(
 *         habit = habit,
 *         onCompleteClick = { viewModel.toggleHabitCompletion(habit.id) }
 *     )
 * }
 *
 * // En filtros:
 * filters.forEach { filter ->
 *     .clickable { viewModel.setFilter(filter) }
 * }
 *
 * ---
 *
 * CICLO DE VIDA:
 *
 * 1. Usuario abre pantalla → init() llama loadHabits()
 * 2. ViewModel observa _currentFilter
 * 3. Cuando cambia filtro → DAO.getHabitsByFrecuencia()
 * 4. Flow retorna datos → _habits.value se actualiza
 * 5. UI observa habits → LazyColumn se redibuja
 * 6. Usuario completa hábito → toggleHabitCompletion()
 * 7. DAO.updateHabit() → BD se actualiza
 * 8. Flow detecta cambio → UI se actualiza automáticamente
 */
