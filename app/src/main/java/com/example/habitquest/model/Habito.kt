package com.example.habitquest.model

import java.io.Serializable
import java.util.Date

/**
 * Modelo que representa un HÁBITO individual
 *
 * CAMPOS ACADÉMICOS REQUERIDOS:
 * - id: Identificador único (autoincrement en BD)
 * - nombre: Nombre del hábito
 * - descripcion: Descripción del hábito
 * - dificultad: EASY (10 XP), MEDIUM (20 XP), HARD (40 XP)
 * - frecuencia: DAILY o WEEKLY
 * - xpRecompensa: XP que gana al completar
 * - tipo: Categoría (STUDY, EXERCISE, HEALTH, GENERAL, etc)
 * - fechaCreacion: Cuándo se creó
 * - estado: ACTIVE o ARCHIVED
 */

data class Habito(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val dificultad: String, // "EASY", "MEDIUM", "HARD"
    val frecuencia: String, // "DAILY", "WEEKLY"
    val xpRecompensa: Int, // 10, 20 o 40
    val tipo: String, // "STUDY", "EXERCISE", "HEALTH", "GENERAL"
    val fechaCreacion: String, // Formato: "yyyy-MM-dd"
    val estado: String = "ACTIVE", // "ACTIVE" o "ARCHIVED"
    val horaSugerida: String = "", // Formato: "HH:mm" - Ej: "08:30"
    val emoji: String = "⚔️" // Para mostrar en UI
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * SISTEMA DE DIFICULTAD Y XP:
 *
 * EASY (10 XP):
 *   - Hábitos simples: beber agua, meditar 5 min
 *   - El usuario puede hacerlos sin esfuerzo
 *
 * MEDIUM (20 XP):
 *   - Hábitos moderados: estudiar 30 min, caminar 20 min
 *   - Requieren dedicación pero son alcanzables
 *
 * HARD (40 XP):
 *   - Hábitos desafiantes: ejercicio 1h, aprender nuevo idioma
 *   - El usuario debe estar motivado
 *
 * ESTO ES IMPORTANTE ACADÉMICAMENTE porque:
 * 1. Gamificación: Recompensa proporcional al esfuerzo
 * 2. Motivación: El usuario elige su dificultad
 * 3. Datos: Podemos analizar qué hábitos completa más
 *
 * ---
 *
 * FRECUENCIA:
 *
 * DAILY:
 *   - Se debe completar TODOS LOS DÍAS
 *   - Afecta la racha
 *   - Ej: "Morning Exercise", "Read 20 pages"
 *
 * WEEKLY:
 *   - Se puede completar en cualquier día de la semana
 *   - No afecta racha diaria (podría tener su propia racha semanal)
 *   - Ej: "Team sports", "Family dinner"
 *
 * ---
 *
 * TIPO:
 *
 * Esto es para ESTADÍSTICAS:
 * - STUDY → +Inteligencia
 * - EXERCISE → +Fuerza
 * - HEALTH → +Disciplina
 * - GENERAL → +Consistencia
 *
 * Esto hace el sistema más interesante para presentar
 */

