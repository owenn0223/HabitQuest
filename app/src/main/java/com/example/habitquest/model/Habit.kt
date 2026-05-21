package com.example.habitquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Modelo de datos para un HÁBITO individual
 *
 * Este modelo representa cada hábito que el usuario crea y gestiona
 * en la pantalla "Lista de Hábitos".
 *
 * CAMPOS ACADÉMICOS REQUERIDOS:
 * - id: Identificador único (autoincrement en BD)
 * - nombre: Nombre del hábito (ej: "Morning Meditation")
 * - frecuencia: Frecuencia del hábito ("DAILY", "WEEKLY", "MONTHLY")
 * - dificultad: Nivel de dificultad ("EASY", "MED", "HARD")
 * - xp: Puntos de experiencia que otorga al completarse
 * - completado: Estado actual (true si completado hoy, false si no)
 * - fechaCreacion: Fecha cuando se creó el hábito (formato "yyyy-MM-dd")
 * - ultimaVezCompletado: Última fecha que se completó (formato "yyyy-MM-dd")
 *
 * NOTA IMPORTANTE:
 * - El campo 'completado' indica si está completado HOY, no históricamente
 * - Para historial completo, usaríamos ProgressoDiario (pero aquí es simplificado)
 * - La frecuencia determina cómo se filtra en la UI
 */

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val frecuencia: String, // "DAILY", "WEEKLY", "MONTHLY"
    val dificultad: String, // "EASY", "MED", "HARD"
    val xp: Int, // Puntos de experiencia (10, 20, 40, etc.)
    val completado: Boolean = false, // Estado actual (completado hoy?)
    val fechaCreacion: String, // Formato: "yyyy-MM-dd"
    val ultimaVezCompletado: String = "" // Formato: "yyyy-MM-dd" (vacío si nunca completado)
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿Por qué estos campos?
 *
 * 1. ID ÚNICO:
 *    - @PrimaryKey(autoGenerate = true)
 *    - Permite identificar cada hábito de forma única
 *    - Necesario para CRUD operations
 *
 * 2. NOMBRE:
 *    - String descriptivo (ej: "Morning Meditation")
 *    - Lo que se muestra en la UI
 *
 * 3. FRECUENCIA:
 *    - "DAILY": Se debe completar todos los días
 *    - "WEEKLY": Se puede completar una vez por semana
 *    - "MONTHLY": Se puede completar una vez al mes
 *    - Se usa para FILTRAR en la UI (All, Daily, Weekly, Monthly)
 *
 * 4. DIFICULTAD:
 *    - "EASY": 10 XP (hábitos simples)
 *    - "MED": 20 XP (hábitos moderados)
 *    - "HARD": 40 XP (hábitos desafiantes)
 *    - Determina el color del badge en UI
 *    - Determina los XP que otorga
 *
 * 5. XP:
 *    - Int que representa puntos de experiencia
 *    - Se muestra en la UI como "50 XP"
 *    - Se usa para gamificación (aunque aquí no se calcula nivel)
 *
 * 6. COMPLETADO:
 *    - Boolean: true = completado hoy, false = no completado
 *    - Determina si mostrar ✓ o +
 *    - Se actualiza cuando el usuario presiona el botón
 *
 * 7. FECHA CREACIÓN:
 *    - String en formato "yyyy-MM-dd"
 *    - Para saber cuándo se creó el hábito
 *    - Útil para estadísticas (opcional)
 *
 * 8. ÚLTIMA VEZ COMPLETADO:
 *    - String en formato "yyyy-MM-dd"
 *    - Para saber cuándo se completó por última vez
 *    - Útil para resetear estado diario
 *
 * ---
 *
 * EJEMPLOS DE DATOS:
 *
 * Habit(
 *     id = 1,
 *     nombre = "Morning Meditation",
 *     frecuencia = "DAILY",
 *     dificultad = "EASY",
 *     xp = 10,
 *     completado = true,
 *     fechaCreacion = "2024-01-15",
 *     ultimaVezCompletado = "2024-01-16"
 * )
 *
 * Habit(
 *     id = 2,
 *     nombre = "Heavy Lifting Session",
 *     frecuencia = "WEEKLY",
 *     dificultad = "HARD",
 *     xp = 40,
 *     completado = false,
 *     fechaCreacion = "2024-01-10",
 *     ultimaVezCompletado = "2024-01-12"
 * )
 *
 * ---
 *
 * RELACIÓN CON LA UI:
 *
 * En HabitsListScreen.kt, cada HabitCard mostrará:
 * - nombre → "Morning Meditation"
 * - frecuencia → Badge "DAILY" (verde)
 * - dificultad → Badge "EASY" (verde)
 * - xp → "10 XP"
 * - completado → Si true: mostrar ✓, si false: mostrar +
 *
 * Los filtros funcionarán así:
 * - "All": Mostrar todos los hábitos
 * - "Daily": Mostrar solo donde frecuencia == "DAILY"
 * - "Weekly": Mostrar solo donde frecuencia == "WEEKLY"
 * - "Monthly": Mostrar solo donde frecuencia == "MONTHLY"
 *
 * ---
 *
 * NOTA SOBRE ROOM:
 *
 * Este modelo está preparado para Room:
 * - @Entity(tableName = "habit") → Crea tabla "habit"
 * - @PrimaryKey(autoGenerate = true) → ID autoincrement
 * - Campos simples (String, Int, Boolean) → Compatibles con SQLite
 *
 * En el PASO 2 crearemos el DAO y Database.
 */
