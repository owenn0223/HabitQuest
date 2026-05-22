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

class CreateHabitViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        NOMBRE_ARCHIVO_HABITOS,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val NOMBRE_ARCHIVO_HABITOS = "habitquest_habitos"
        private const val CLAVE_LISTA_HABITOS = "lista_habitos_json"
    }

    private val _habitName = MutableStateFlow("")
    val habitName: StateFlow<String> = _habitName

    private val _selectedFrequency = MutableStateFlow("DAILY")
    val selectedFrequency: StateFlow<String> = _selectedFrequency

    private val _selectedDifficulty = MutableStateFlow("EASY")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty

    private val _selectedAttribute = MutableStateFlow("Strength")
    val selectedAttribute: StateFlow<String> = _selectedAttribute

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    fun setHabitName(name: String) {
        _habitName.value = name
        if (_errorMessage.value != null) {
            _errorMessage.value = null
        }
    }

    fun setFrequency(frequency: String) {
        _selectedFrequency.value = frequency
    }

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    fun setAttribute(attribute: String) {
        _selectedAttribute.value = attribute
    }

    fun createHabit(onSuccess: () -> Unit = {}) {
        if (_habitName.value.isBlank()) {
            _errorMessage.value = "El nombre del hábito no puede estar vacío"
            return
        }
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
                // XP base según dificultad
                val xpBase = when (_selectedDifficulty.value) {
                    "EASY" -> 10
                    "MED"  -> 20
                    "HARD" -> 40
                    else   -> 10
                }

                // XP final según frecuencia (mayor frecuencia = mayor recompensa)
                val xp = when (_selectedFrequency.value) {
                    "WEEKLY"  -> xpBase * 2
                    "MONTHLY" -> xpBase * 4
                    else      -> xpBase // DAILY
                }

                val newHabit = Habit(
                    nombre = _habitName.value.trim(),
                    frecuencia = _selectedFrequency.value,
                    dificultad = _selectedDifficulty.value,
                    xp = xp,
                    completado = false,
                    fechaCreacion = getCurrentDate(),
                    ultimaVezCompletado = ""
                )

                habitDao.insertHabit(newHabit)
                saveHabitToSharedPreferences(newHabit)

                _successMessage.value = "¡Hábito creado exitosamente! 🎉"
                resetForm()
                onSuccess()

            } catch (e: Exception) {
                _errorMessage.value = "Error al crear hábito: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveHabitToSharedPreferences(habit: Habit) {
        try {
            val habitosJson = sharedPreferences.getString(CLAVE_LISTA_HABITOS, "[]") ?: "[]"
            val habitosArray = JSONArray(habitosJson)
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
            habitosArray.put(habitJson)
            sharedPreferences.edit().apply {
                putString(CLAVE_LISTA_HABITOS, habitosArray.toString())
                apply()
            }
        } catch (e: Exception) {
            println("Error guardando en SharedPreferences: ${e.message}")
        }
    }

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

    fun clearErrorMessage() { _errorMessage.value = null }
    fun clearSuccessMessage() { _successMessage.value = null }

    private fun resetForm() {
        _habitName.value = ""
        _selectedFrequency.value = "DAILY"
        _selectedDifficulty.value = "EASY"
        _selectedAttribute.value = "Strength"
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentXp(): Int {
        val xpBase = when (_selectedDifficulty.value) {
            "EASY" -> 10
            "MED"  -> 20
            "HARD" -> 40
            else   -> 10
        }
        return when (_selectedFrequency.value) {
            "WEEKLY"  -> xpBase * 2
            "MONTHLY" -> xpBase * 4
            else      -> xpBase
        }
    }

    fun isValidHabitName(): Boolean {
        val name = _habitName.value.trim()
        return name.length >= 3 && name.length <= 50
    }
}
