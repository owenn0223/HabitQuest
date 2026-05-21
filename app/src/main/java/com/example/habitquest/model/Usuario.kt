package com.example.habitquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Modelo que representa un usuario (personaje RPG)
 *
 * CAMPOS ACADÉMICOS REQUERIDOS:
 * - id: Identificador único
 * - nombre: Nombre del personaje
 * - correo: Correo electrónico para login
 * - contraseña: Contraseña para login
 * - clase: Tipo de personaje (Warrior, Mage, Sage, Adventurer)
 * - nivelActual: Nivel actual del personaje
 * - xpActual: XP acumulado en el nivel actual
 * - xpTotal: XP total acumulado en toda la app
 * - rachaActual: Días consecutivos completando hábitos
 */

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val clase: String, // "WARRIOR", "MAGE", "SAGE", "ADVENTURER"
    val nivelActual: Int = 1,
    val xpActual: Int = 0, // XP dentro del nivel actual (0-100)
    val xpTotal: Int = 0, // Total acumulado
    val rachaActual: Int = 0, // Días consecutivos
    val ultimaFecha: String = "", // Última fecha que completó un hábito (para racha)

    // Estadísticas RPG
    val disciplina: Int = 10,
    val fuerza: Int = 10,
    val inteligencia: Int = 10,
    val consistencia: Int = 10
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿Por qué estos campos?
 *
 * 1. NIVEL Y XP:
 *    - nivelActual: Muestra progreso visible (nivel 1, 2, 3...)
 *    - xpActual: XP faltante para subir de nivel (0-100)
 *    - xpTotal: Métrica de esfuerzo total del usuario
 *
 *    FÓRMULA DE NIVELES:
 *    - Nivel 1: 0 XP
 *    - Nivel 2: 100 XP
 *    - Nivel 3: 200 XP
 *    - Fórmula: XP_requerido = nivel * 100
 *
 * 2. RACHA:
 *    - rachaActual: Motivación psicológica ("no romper la cadena")
 *    - ultimaFecha: Verifica si completó hoy (si no, racha = 0)
 *
 * 3. ESTADÍSTICAS:
 *    - Disciplina: Aumenta con hábitos diarios
 *    - Fuerza: Aumenta con ejercicio
 *    - Inteligencia: Aumenta con estudio
 *    - Consistencia: Aumenta con cualquier hábito
 *
 *    Esto es lo que hace el sistema "RPG real"
 */
