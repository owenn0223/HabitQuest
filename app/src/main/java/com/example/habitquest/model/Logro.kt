package com.example.habitquest.model

import java.io.Serializable

/**
 * Modelo que representa un LOGRO/ACHIEVEMENT que el usuario puede desbloquear
 *
 * CAMPOS ACADÉMICOS REQUERIDOS:
 * - id: Identificador único
 * - nombre: Nombre del logro
 * - descripcion: Descripción breve
 * - icono: Emoji o ícono del logro
 * - condicion: Tipo de condición (STREAK, LEVEL, TOTAL_XP, HABITS_COMPLETED)
 * - valor: Valor necesario (ej: streak de 7 días)
 * - desbloqueado: Si el usuario lo logró
 * - fechaDesbloqueo: Cuándo lo desbloqueó
 */

data class Logro(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val icono: String = "🏆",
    val condicion: String, // "STREAK", "LEVEL", "TOTAL_XP", "HABITS_COMPLETED"
    val valor: Int, // Ej: 7 (para 7 días de racha)
    val desbloqueado: Boolean = false,
    val fechaDesbloqueo: String = "" // Formato: "yyyy-MM-dd"
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * TIPOS DE LOGROS Y CÓMO SE CALCULAN:
 *
 * 1. STREAK (Racha):
 *    - Nombre: "7 Day Champion"
 *    - Condición: STREAK
 *    - Valor: 7
 *    - Se desbloquea cuando: rachaActual == 7
 *
 * 2. LEVEL (Nivel):
 *    - Nombre: "Rising Hero"
 *    - Condición: LEVEL
 *    - Valor: 5
 *    - Se desbloquea cuando: nivelActual >= 5
 *
 * 3. TOTAL_XP (XP Total):
 *    - Nombre: "Experience Seeker"
 *    - Condición: TOTAL_XP
 *    - Valor: 1000
 *    - Se desbloquea cuando: xpTotal >= 1000
 *
 * 4. HABITS_COMPLETED (Hábitos Completados):
 *    - Nombre: "Habit Collector"
 *    - Condición: HABITS_COMPLETED
 *    - Valor: 100
 *    - Se desbloquea cuando: COUNT(ProgressoDiario.completado=TRUE) >= 100
 *
 * ---
 *
 * EJEMPLOS DE LOGROS GAMIFICADOS:
 *
 * 🟢 FÁCILES (Sin recompensa):
 *    "First Step" - Crear primer hábito
 *    "Getting Started" - Completar primer hábito
 *
 * 🟡 MEDIOS (Requieren dedicación):
 *    "One Week Wonder" - 7 días de racha
 *    "Level Up!" - Alcanzar nivel 2
 *    "50 XP Grind" - Acumular 50 XP total
 *
 * 🔴 DIFÍCILES (Requieren compromiso):
 *    "Legend Status" - 30 días de racha
 *    "Master of Habits" - 100 hábitos completados
 *    "XP Millionaire" - 5000 XP total
 *
 * ---
 *
 * MOTIVACIÓN PSICOLÓGICA:
 *
 * Los logros funcionan porque:
 * 1. Son metas visuales (medallas desbloqueadas)
 * 2. Dan sensación de progreso
 * 3. Crean competencia consigo mismo
 * 4. Son celebrables (se puede mostrar notificación)
 *
 * ESTO ES EXCELENTE para la presentación académica:
 * "Mediante un sistema de logros, incentivamos la retención
 *  del usuario y la motivación continua"
 */

