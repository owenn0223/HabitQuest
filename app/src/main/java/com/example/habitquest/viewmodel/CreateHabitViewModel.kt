package com.example.habitquest.viewmodel

import android.app.Application
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

class CreateHabitViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val habitDao = database.habitDao()

    // ---- Estado de cada campo del formulario ----
    val nombre = MutableStateFlow("")
    val descripcion = MutableStateFlow("")
    val frecuencia = MutableStateFlow("DAILY")
    val dificultad = MutableStateFlow("MED")
    val tipo = MutableStateFlow("Strength")
    val emoji = MutableStateFlow("⚔️")

    // ---- Estado de error y éxito ----
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _habitCreado = MutableStateFlow(false)
    val habitCreado: StateFlow<Boolean> = _habitCreado

    // ---- Calcular XP según dificultad ----
    private fun calcularXP(): Int {
        return when (dificultad.value) {
            "EASY" -> 10
            "MED"  -> 20
            "HARD" -> 40
            else   -> 10
        }
    }

    // ---- Validar campos antes de guardar ----
    private fun validar(): Boolean {
        return when {
            nombre.value.isBlank() -> {
                _errorMessage.value = "El nombre del hábito no puede estar vacío"
                false
            }
            nombre.value.length < 3 -> {
                _errorMessage.value = "El nombre debe tener al menos 3 caracteres"
                false
            }
            else -> {
                _errorMessage.value = null
                true
            }
        }
    }

    // ---- Guardar hábito en Room ----
    fun guardarHabito() {
        if (!validar()) return

        viewModelScope.launch {
            val nuevoHabito = Habit(
                nombre = nombre.value.trim(),
                descripcion = descripcion.value.trim(),
                frecuencia = frecuencia.value,
                dificultad = dificultad.value,
                tipo = tipo.value,
                emoji = emoji.value,
                xp = calcularXP(),
                fechaCreacion = getFechaActual()
            )
            habitDao.insertHabit(nuevoHabito)
            _habitCreado.value = true // Le avisa a la UI que puede navegar atrás
        }
    }

    // ---- Limpiar error cuando el usuario empieza a escribir ----
    fun limpiarError() {
        _errorMessage.value = null
    }

    // ---- Fecha actual ----
    private fun getFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}