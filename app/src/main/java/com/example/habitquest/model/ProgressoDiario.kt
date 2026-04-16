package com.example.habitquest.model

import java.io.Serializable

/**
 * Modelo que registra si un hábito fue completado en un día específico
 *
 * CAMPOS ACADÉMICOS REQUERIDOS:
 * - id: Identificador único (autoincrement)
 * - habitoId: ID del hábito (relación con tabla Habito)
 * - fecha: Día específico (formato "yyyy-MM-dd")
 * - completado: TRUE si se completó ese día, FALSE si no
 * - xpGanado: XP que ganó el usuario ese día (0 si no completó)
 *
 * NOTA IMPORTANTE: Esta tabla es el "historial" de todo
 */

data class ProgressoDiario(
    val id: Int = 0,
    val habitoId: Int,
    val fecha: String, // Formato: "yyyy-MM-dd"
    val completado: Boolean = false,
    val xpGanado: Int = 0,
    val horaCompletado: String = "" // Opcional: a qué hora completó
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿Por qué necesitamos esta tabla?
 *
 * CÁLCULO DE ESTADÍSTICAS EN EL DASHBOARD:
 *
 * 1. "Habits Today" (4/8):
 *    - Contar ProgressoDiario donde fecha = HOY y completado = TRUE
 *    - Resultado: 4 de 8 hábitos completados
 *
 * 2. "Current Streak" (15 days):
 *    - Para cada día, verificar si TODOS los hábitos DAILY fueron completados
 *    - Contar días consecutivos sin "fallar"
 *    - Si falta un día → racha = 0
 *
 * 3. "Total Lifetime XP":
 *    - Sumar TODOS los xpGanado de toda la tabla
 *
 * 4. "Habits List":
 *    - Mostrar todos los Habito
 *    - Para cada uno, mostrar último ProgressoDiario (si completó hoy)
 *
 * ---
 *
 * EJEMPLO VISUAL:
 *
 * Tabla HABITO:
 * ┌─────┬──────────────────┬────────────────┐
 * │ id  │ nombre           │ xpRecompensa   │
 * ├─────┼──────────────────┼────────────────┤
 * │ 1   │ Morning Exercise │ 40 XP (HARD)   │
 * │ 2   │ Read 20 Pages    │ 20 XP (MEDIUM) │
 * │ 3   │ Drink 8 Glasses  │ 10 XP (EASY)   │
 * └─────┴──────────────────┴────────────────┘
 *
 * Tabla PROGRESO_DIARIO:
 * ┌─────┬──────────┬────────────┬───────────┬──────────┐
 * │ id  │ habitoId │ fecha      │ completado│ xpGanado │
 * ├─────┼──────────┼────────────┼───────────┼──────────┤
 * │ 1   │ 1        │ 2024-01-15 │ TRUE      │ 40       │
 * │ 2   │ 2        │ 2024-01-15 │ TRUE      │ 20       │
 * │ 3   │ 3        │ 2024-01-15 │ FALSE     │ 0        │
 * │ 4   │ 1        │ 2024-01-16 │ TRUE      │ 40       │
 * │ 5   │ 2        │ 2024-01-16 │ TRUE      │ 20       │
 * │ 6   │ 3        │ 2024-01-16 │ TRUE      │ 10       │
 * └─────┴──────────┴────────────┴───────────┴──────────┘
 *
 * ANÁLISIS:
 * - Hoy (15 de enero): 2 de 3 completados
 * - XP ganado hoy: 60 (40 + 20)
 * - Racha: No se cuenta porque falta el día 15 completo
 *
 * ---
 *
 * QUERIES QUE USAREMOS:
 *
 * 1. Hábitos completados hoy:
 *    SELECT COUNT(*) FROM progreso_diario
 *    WHERE fecha = HOY AND completado = TRUE
 *
 * 2. Total hábitos:
 *    SELECT COUNT(DISTINCT habitoId) FROM habito WHERE estado = 'ACTIVE'
 *
 * 3. XP total acumulado:
 *    SELECT SUM(xpGanado) FROM progreso_diario
 *
 * 4. Racha actual:
 *    Bucle verificando si cada día pasado tiene todos completados
 *    Se detiene en el primer día incompleto
 */

