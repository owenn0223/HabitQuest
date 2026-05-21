package com.example.habitquest.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Dificultad y XP
const val DIFICULTAD_EASY = "EASY"      // 10 XP
const val DIFICULTAD_MEDIUM = "MEDIUM"  // 20 XP
const val DIFICULTAD_HARD = "HARD"      // 40 XP

// Frecuencia
const val FRECUENCIA_DAILY = "DAILY"
const val FRECUENCIA_WEEKLY = "WEEKLY"

// Tipo de hábito
const val TIPO_STUDY = "STUDY"
const val TIPO_EXERCISE = "EXERCISE"
const val TIPO_HEALTH = "HEALTH"
const val TIPO_GENERAL = "GENERAL"

// Estado
const val ESTADO_ACTIVE = "ACTIVE"
const val ESTADO_ARCHIVED = "ARCHIVED"

// Condiciones de logros
const val CONDICION_STREAK = "STREAK"
const val CONDICION_LEVEL = "LEVEL"
const val CONDICION_TOTAL_XP = "TOTAL_XP"
const val CONDICION_HABITS_COMPLETED = "HABITS_COMPLETED"

fun getXPPorDificultad(dificultad: String): Int = when (dificultad) {
    DIFICULTAD_EASY -> 10
    DIFICULTAD_MEDIUM -> 20
    DIFICULTAD_HARD -> 40
    else -> 0
}

data class NivelInfo(
    val nivel: Int,
    val xpEnNivel: Int,
    val xpParaSiguiente: Int,
    val porcentaje: Float
)

fun calcularNivel(xpTotal: Int): NivelInfo {
    var nivel = 1
    var xpAcumulado = 0
    while (xpTotal >= xpAcumulado + (nivel * 100)) {
        xpAcumulado += nivel * 100
        nivel++
    }
    val xpEnNivel = xpTotal - xpAcumulado
    val xpParaSiguiente = nivel * 100
    val porcentaje = xpEnNivel.toFloat() / xpParaSiguiente
    return NivelInfo(nivel, xpEnNivel, xpParaSiguiente, porcentaje)
}

fun fechaActual(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}

fun sumarDias(fecha: String, dias: Int): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = sdf.parse(fecha) ?: Date()
    calendar.add(Calendar.DAY_OF_YEAR, dias)
    return sdf.format(calendar.time)
}
