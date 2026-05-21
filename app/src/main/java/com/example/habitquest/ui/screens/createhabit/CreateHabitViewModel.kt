package com.example.habitquest.ui.screens.createhabit

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

/**
 * ViewModel para la pantalla de Crear Hábito (CreateHabitScreen)
 *
 * Este ViewModel maneja:
 * - Captura de datos del formulario de creación
 * - Validación de datos
 * - Creación y guardado del hábito (Room DB + SharedPreferences)
 * - Notificaciones de estado (éxito, error)
 * - Estado reactivo con StateFlow
 *
 * CARACTERÍSTICAS ACADÉMICAS:
 * - AndroidViewModel: Para acceso a Application context
 * - StateFlow: Para estado observable
 * - SharedPreferences: Para persistencia de hábitos (respaldo)
 * - Room Database: Para persistencia principal
 * - viewModelScope: Para corrutinas que sobreviven a cambios de configuración
 */

class CreateHabitViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de la base de datos Room
    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()

    // SharedPreferences para respaldo
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        NOMBRE_ARCHIVO_HABITOS,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val NOMBRE_ARCHIVO_HABITOS = "habitquest_habitos"
        private const val CLAVE_LISTA_HABITOS = "lista_habitos_json"
    }

    // ====== ESTADO DEL FORMULARIO ======
    private val _habitName = MutableStateFlow("")
    val habitName: StateFlow<String> = _habitName

    private val _selectedFrequency = MutableStateFlow("DAILY")
    val selectedFrequency: StateFlow<String> = _selectedFrequency

    private val _selectedDifficulty = MutableStateFlow("EASY")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty

    private val _selectedAttribute = MutableStateFlow("Strength")
    val selectedAttribute: StateFlow<String> = _selectedAttribute

    // ====== ESTADO DE LA UI ======
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // ====== MÉTODOS DE ACTUALIZACIÓN DEL ESTADO ======

    /**
     * ACTUALIZAR NOMBRE DEL HÁBITO
     *
     * @param name Nuevo nombre del hábito
     */
    fun setHabitName(name: String) {
        _habitName.value = name
        // Limpiar errores cuando el usuario empieza a escribir
        if (_errorMessage.value != null) {
            _errorMessage.value = null
        }
    }

    /**
     * ACTUALIZAR FRECUENCIA SELECCIONADA
     *
     * @param frequency Frecuencia seleccionada ("DAILY", "WEEKLY", "MONTHLY")
     */
    fun setFrequency(frequency: String) {
        _selectedFrequency.value = frequency
    }

    /**
     * ACTUALIZAR DIFICULTAD SELECCIONADA
     *
     * @param difficulty Dificultad seleccionada ("EASY", "MED", "HARD")
     */
    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    /**
     * ACTUALIZAR ATRIBUTO RPG SELECCIONADO
     *
     * @param attribute Atributo seleccionado ("Strength", "Intelligence", "Agility", "Charisma")
     */
    fun setAttribute(attribute: String) {
        _selectedAttribute.value = attribute
    }

    /**
     * CREAR NUEVO HÁBITO
     *
     * Valida los datos, calcula XP según dificultad, y guarda en Room + SharedPreferences
     * Emite mensajes de éxito o error
     *
     * @param onSuccess Callback cuando se crea exitosamente
     */
    fun createHabit(onSuccess: () -> Unit = {}) {
        // Validar que el nombre no esté vacío
        if (_habitName.value.isBlank()) {
            _errorMessage.value = "El nombre del hábito no puede estar vacío"
            return
        }

        // Validar que el nombre tenga una longitud razonable
        if (_habitName.value.length < 3) {
            _errorMessage.value = "El nombre debe tener al menos 3 caracteres"
            return
        }

        if (_habitName.value.length > 50) {
            _errorMessage.value = "El nombre no puede superar 50 caracteres"
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Calcular XP según dificultad
                val xp = when (_selectedDifficulty.value) {
                    "EASY" -> 10
                    "MED" -> 20
                    "HARD" -> 40
                    else -> 10
                }

                // Crear objeto Habit
                val newHabit = Habit(
                    nombre = _habitName.value.trim(),
                    frecuencia = _selectedFrequency.value,
                    dificultad = _selectedDifficulty.value,
                    xp = xp,
                    completado = false,
                    fechaCreacion = getCurrentDate(),
                    ultimaVezCompletado = ""
                )

                // GUARDAR EN ROOM DATABASE
                habitDao.insertHabit(newHabit)

                // GUARDAR EN SHAREPREFERENCES (respaldo)
                saveHabitToSharedPreferences(newHabit)

                // Mostrar mensaje de éxito
                _successMessage.value = "¡Hábito creado exitosamente! 🎉"

                // Resetear formulario
                resetForm()

                // Llamar callback de éxito
                onSuccess()

            } catch (e: Exception) {
                _errorMessage.value = "Error al crear hábito: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * GUARDAR HÁBITO EN SHAREPREFERENCES
     *
     * Serializa el hábito a JSON y lo agrega a la lista en SharedPreferences
     * Esto proporciona respaldo o alternativa a Room
     *
     * @param habit Hábito a guardar
     */
    private fun saveHabitToSharedPreferences(habit: Habit) {
        try {
            // Obtener lista actual de hábitos
            val habitosJson = sharedPreferences.getString(CLAVE_LISTA_HABITOS, "[]") ?: "[]"
            val habitosArray = JSONArray(habitosJson)

            // Convertir hábito a JSONObject
            val habitJson = JSONObject().apply {
                put("id", habit.id)
                put("nombre", habit.nombre)
                put("frecuencia", habit.frecuencia)
                put("dificultad", habit.dificultad)
                put("xp", habit.xp)
                put("completado", habit.completado)
                put("fechaCreacion", habit.fechaCreacion)
                put("ultimaVezCompletado", habit.ultimaVezCompletado)
            }

            // Agregar nuevo hábito al array
            habitosArray.put(habitJson)

            // Guardar en SharedPreferences
            sharedPreferences.edit().apply {
                putString(CLAVE_LISTA_HABITOS, habitosArray.toString())
                apply()
            }
        } catch (e: Exception) {
            // Log del error pero no interrumpir el flujo
            println("Error guardando en SharedPreferences: ${e.message}")
        }
    }

    /**
     * OBTENER LISTA DE HÁBITOS DESDE SHAREPREFERENCES
     *
     * @return Lista de hábitos recuperados de SharedPreferences
     */
    fun getHabitsFromSharedPreferences(): List<Habit> {
        return try {
            val habitosJson = sharedPreferences.getString(CLAVE_LISTA_HABITOS, "[]") ?: "[]"
            val habitosArray = JSONArray(habitosJson)
            val habitos = mutableListOf<Habit>()

            for (i in 0 until habitosArray.length()) {
                val habitJson = habitosArray.getJSONObject(i)
                val habit = Habit(
                    id = habitJson.getInt("id"),
                    nombre = habitJson.getString("nombre"),
                    frecuencia = habitJson.getString("frecuencia"),
                    dificultad = habitJson.getString("dificultad"),
                    xp = habitJson.getInt("xp"),
                    completado = habitJson.getBoolean("completado"),
                    fechaCreacion = habitJson.getString("fechaCreacion"),
                    ultimaVezCompletado = habitJson.getString("ultimaVezCompletado")
                )
                habitos.add(habit)
            }
            habitos
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * LIMPIAR MENSAJE DE ERROR
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /**
     * LIMPIAR MENSAJE DE ÉXITO
     */
    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    /**
     * RESETEAR FORMULARIO
     *
     * Limpia todos los campos del formulario
     */
    private fun resetForm() {
        _habitName.value = ""
        _selectedFrequency.value = "DAILY"
        _selectedDifficulty.value = "EASY"
        _selectedAttribute.value = "Strength"
    }

    /**
     * OBTENER FECHA ACTUAL
     *
     * @return Fecha actual en formato "yyyy-MM-dd"
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * OBTENER XP SEGÚN DIFICULTAD
     *
     * Método auxiliar para calcular XP
     *
     * @return XP correspondiente a la dificultad actual
     */
    fun getCurrentXp(): Int {
        return when (_selectedDifficulty.value) {
            "EASY" -> 10
            "MED" -> 20
            "HARD" -> 40
            else -> 10
        }
    }

    /**
     * VALIDAR NOMBRE DEL HÁBITO
     *
     * @return true si el nombre es válido, false en caso contrario
     */
    fun isValidHabitName(): Boolean {
        val name = _habitName.value.trim()
        return name.length >= 3 && name.length <= 50
    }
}

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿POR QUÉ ESTE VIEWMODEL?
 *
 * 1. SEPARACIÓN DE RESPONSABILIDADES:
 *    - ViewModel: Maneja estado y lógica de creación
 *    - DAO: Maneja operaciones de BD
 *    - UI: Solo muestra datos y captura entrada
 *
 * 2. OBSERVABILIDAD CON STATEFLOW:
 *    - Todos los campos del formulario son StateFlow
 *    - La UI se actualiza automáticamente cuando cambian
 *    - Patrón MVVM (Model-View-ViewModel)
 *
 * 3. VALIDACIÓN:
 *    - isValidHabitName(): Valida antes de guardar
 *    - Previene hábitos sin nombre o con caracteres inválidos
 *
 * 4. GUARDADO DUAL:
 *    - Room Database: Persistencia principal
 *    - SharedPreferences: Respaldo/alternativa para lectura rápida
 *
 * 5. SERIALIZACIÓN JSON:
 *    - JSONObject: Para convertir Habit a JSON
 *    - JSONArray: Para mantener lista de hábitos
 *    - Permite portabilidad de datos
 *
 * 6. MENSAJES DE ESTADO:
 *    - _errorMessage: Para mostrar errores
 *    - _successMessage: Para confirmación de éxito
 *    - _isLoading: Para mostrar indicador de carga
 *
 * ---
 *
 * CICLO DE VIDA:
 *
 * 1. Usuario abre CreateHabitScreen
 * 2. Se crea instancia del ViewModel
 * 3. Usuario rellena el formulario
 * 4. Datos se guardan en StateFlow (setHabitName, setFrequency, etc.)
 * 5. Usuario presiona "BEGIN QUEST"
 * 6. createHabit() valida y guarda en Room + SharedPreferences
 * 7. Si éxito: mostrar mensaje y resetear
 * 8. Si error: mostrar mensaje de error
 *
 * ---
 *
 * INTEGRACIÓN CON SHAREPREFERENCES:
 *
 * Los hábitos se guardan en JSON dentro de SharedPreferences:
 *
 * CLAVE: "lista_habitos_json"
 * VALOR: [
 *   {
 *     "id": 1,
 *     "nombre": "Morning Meditation",
 *     "frecuencia": "DAILY",
 *     "dificultad": "EASY",
 *     "xp": 10,
 *     "completado": false,
 *     "fechaCreacion": "2024-01-15",
 *     "ultimaVezCompletado": ""
 *   },
 *   ...
 * ]
 *
 * Esto permite:
 * - Recuperar hábitos incluso sin acceso a Room
 * - Sincronización entre dispositivos (si necesario)
 * - Backup manual de datos
 */

