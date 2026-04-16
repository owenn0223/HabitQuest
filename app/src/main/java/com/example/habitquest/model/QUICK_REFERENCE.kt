package com.example.habitquest.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🎯 QUICK REFERENCE - ESTRUCTURA DE DATOS HABITQUEST
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Guía rápida para consultar durante el desarrollo
 */

/**
 * TABLA 1: USUARIO
 *
 * Almacena: Datos del personaje
 * Count: 1 solo registro por app
 *
 * CAMPOS:
 * - id: Int (1)
 * - nombre: String
 * - clase: String (WARRIOR, MAGE, SAGE, ADVENTURER)
 * - nivelActual: Int
 * - xpActual: Int (0-100, XP dentro del nivel)
 * - xpTotal: Int (XP histórico)
 * - rachaActual: Int
 * - ultimaFecha: String (yyyy-MM-dd)
 * - disciplina: Int
 * - fuerza: Int
 * - inteligencia: Int
 * - consistencia: Int
 *
 * QUERIES QUE USAREMOS:
 * - getUsuario(id: Int)
 * - updateUsuario(usuario: Usuario)
 * - updateRacha(nuevoValor: Int)
 * - updateXP(nuevoXP: Int)
 * - updateNivel(nuevoNivel: Int)
 * - updateEstadisticas(disciplina, fuerza, inteligencia, consistencia)
 */

/**
 * TABLA 2: HABITO
 *
 * Almacena: Cada hábito creado por el usuario
 * Count: Varios (7-20 típicamente)
 *
 * CAMPOS:
 * - id: Int (autoincrement)
 * - nombre: String
 * - descripcion: String
 * - dificultad: String (EASY, MEDIUM, HARD)
 * - frecuencia: String (DAILY, WEEKLY)
 * - xpRecompensa: Int (10, 20 o 40)
 * - tipo: String (STUDY, EXERCISE, HEALTH, GENERAL)
 * - fechaCreacion: String (yyyy-MM-dd)
 * - estado: String (ACTIVE, ARCHIVED)
 * - horaSugerida: String (HH:mm)
 * - emoji: String
 *
 * QUERIES QUE USAREMOS:
 * - getHabitosActivos(): List<Habito>
 * - getHabitoDiarios(): List<Habito> (WHERE frecuencia = 'DAILY')
 * - insertHabito(habito: Habito)
 * - updateHabito(habito: Habito)
 * - deleteHabito(habitoId: Int)
 * - getHabitoById(id: Int)
 * - getHabitosPorTipo(tipo: String)
 */

/**
 * TABLA 3: PROGRESO_DIARIO (⭐ LA MÁS IMPORTANTE)
 *
 * Almacena: Historial de cada día
 * Count: Miles (1 registro por hábito por día)
 *
 * CAMPOS:
 * - id: Int (autoincrement)
 * - habitoId: Int (FOREIGN KEY → habito.id)
 * - fecha: String (yyyy-MM-dd)
 * - completado: Boolean
 * - xpGanado: Int
 * - horaCompletado: String (HH:mm)
 *
 * QUERIES QUE USAREMOS:
 * - insertProgreso(progreso: ProgressoDiario)
 * - getProgresoDelDia(fecha: String): List<ProgressoDiario>
 * - getProgresoDelHabito(habitoId: Int, fecha: String): ProgressoDiario?
 * - countCompletadosHoy(): Int
 * - getSumXPTotal(): Int
 * - getProgresoRango(fechaInicio: String, fechaFin: String)
 *
 * CÁLCULOS:
 * - Hábitos hoy: COUNT(WHERE fecha=HOY AND completado=TRUE)
 * - XP total: SUM(xpGanado WHERE completado=TRUE)
 * - Racha: LOOP desde HOY hacia atrás
 */

/**
 * TABLA 4: LOGRO
 *
 * Almacena: Achievements desbloqueables
 * Count: 10-15 típicamente
 *
 * CAMPOS:
 * - id: Int
 * - nombre: String
 * - descripcion: String
 * - icono: String
 * - condicion: String (STREAK, LEVEL, TOTAL_XP, HABITS_COMPLETED)
 * - valor: Int
 * - desbloqueado: Boolean
 * - fechaDesbloqueo: String
 *
 * QUERIES QUE USAREMOS:
 * - getAllLogros(): List<Logro>
 * - getLogrosDesbloqueados(): List<Logro>
 * - updateLogroDesbloqueado(logroId: Int, fecha: String)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * CÁLCULOS CENTRALES
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * CÁLCULO 1: HÁBITOS COMPLETADOS HOY
 *
 * SQL:
 * SELECT COUNT(*)
 * FROM progreso_diario
 * WHERE fecha = TODAY
 * AND completado = TRUE
 *
 * Kotlin:
 * val hoyCompletados = progressoDao.countCompletadosHoy(today)
 * val totalDiarios = habitoDao.countDiarios()
 * val resultado = "$hoyCompletados/$totalDiarios"
 *
 * Ejemplo: "5/7" (completó 5 de 7 hábitos diarios hoy)
 */

/**
 * CÁLCULO 2: RACHA ACTUAL
 *
 * Lógica:
 * racha = 0
 * fecha = HOY
 *
 * while true:
 *     totalDiariosEseDia = SELECT COUNT FROM habito WHERE frecuencia='DAILY'
 *     completadosEseDia = SELECT COUNT FROM progreso_diario
 *                         WHERE fecha=fecha AND completado=TRUE
 *
 *     if completadosEseDia == totalDiariosEseDia:
 *         racha++
 *         fecha = fecha - 1 día
 *     else:
 *         break
 *
 * Ejemplo: Si los últimos 15 días completó todos → racha = 15
 * Si hoy o ayer falta 1 → racha = 0
 */

/**
 * CÁLCULO 3: XP TOTAL ACUMULADO
 *
 * SQL:
 * SELECT SUM(xpGanado)
 * FROM progreso_diario
 * WHERE completado = TRUE
 *
 * Kotlin:
 * val xpTotal = progressoDao.getSumXPTotal()
 *
 * Ejemplo: 12,450 XP histórico
 */

/**
 * CÁLCULO 4: NIVEL Y PROGRESO
 *
 * Fórmula:
 * XP_para_nivel_N = N * 100
 *
 * Algoritmo:
 * nivel = 1
 * xpAcumulado = 0
 *
 * while xpTotal >= xpAcumulado + (nivel * 100):
 *     xpAcumulado += nivel * 100
 *     nivel++
 *
 * xpEnNivelActual = xpTotal - xpAcumulado
 * xpParaSiguiente = nivel * 100
 * porcentaje = xpEnNivelActual.toFloat() / xpParaSiguiente
 *
 * Ejemplo con 12,450 XP:
 * - Nivel: 12 (o el que sea según cálculo)
 * - XP en nivel 12: 450
 * - XP para nivel 13: 1200
 * - Progreso: 450/1200 = 37.5%
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * RELACIONES (FOREIGN KEYS)
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * ProgressoDiario.habitoId → Habito.id
 *
 * Relación: Muchos a uno
 * Significado: Un hábito puede tener muchos registros de progreso
 *
 * Ejemplo:
 * Habito (id=1): "Morning Exercise"
 *
 * ProgressoDiario records:
 * - 2024-01-14, habitoId=1, completado=true
 * - 2024-01-15, habitoId=1, completado=true
 * - 2024-01-16, habitoId=1, completado=false
 * (3 registros para el mismo hábito en diferentes días)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * CONSTANTES Y VALORES FIJOS
 * ════════════════════════════════════════════════════════════════════════════════
 */

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

// Clases
const val CLASE_WARRIOR = "WARRIOR"
const val CLASE_MAGE = "MAGE"
const val CLASE_SAGE = "SAGE"
const val CLASE_ADVENTURER = "ADVENTURER"

// Estado
const val ESTADO_ACTIVE = "ACTIVE"
const val ESTADO_ARCHIVED = "ARCHIVED"

// Logros
const val CONDICION_STREAK = "STREAK"
const val CONDICION_LEVEL = "LEVEL"
const val CONDICION_TOTAL_XP = "TOTAL_XP"
const val CONDICION_HABITS_COMPLETED = "HABITS_COMPLETED"

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * FUNCIONES HELPERS QUE NECESITAREMOS
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * 1. Obtener XP basado en dificultad
 */
fun getXPPorDificultad(dificultad: String): Int = when (dificultad) {
    DIFICULTAD_EASY -> 10
    DIFICULTAD_MEDIUM -> 20
    DIFICULTAD_HARD -> 40
    else -> 0
}

/**
 * 2. Calcular nivel y XP actual
 */
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

/**
 * 3. Formatear fecha a string
 */
fun fechaActual(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}

/**
 * 4. Restar/sumar días
 */
fun sumarDias(fecha: String, dias: Int): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = sdf.parse(fecha) ?: Date()
    calendar.add(Calendar.DAY_OF_YEAR, dias)
    return sdf.format(calendar.time)
}

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * TODO LIST PARA IMPLEMENTACIÓN
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * PASO 2: Room Database
 * - [ ] Convertir modelos en @Entity
 * - [ ] Crear @Dao interfaces
 * - [ ] Crear @Database
 * - [ ] Agregar dependencias
 *
 * PASO 3: Repository
 * - [ ] Crear HabitRepository
 * - [ ] Implementar CRUD operations
 * - [ ] Implementar cálculos (racha, nivel, XP)
 *
 * PASO 4: ViewModel
 * - [ ] Crear DashboardViewModel
 * - [ ] Crear CreateHabitViewModel
 * - [ ] Usar Flow/LiveData
 *
 * PASO 5: Conexión UI
 * - [ ] Modificar DashboardScreen
 * - [ ] Modificar CreateHabitScreen
 * - [ ] Modificar HabitsListScreen
 * - [ ] Modificar AchievementsScreen
 *
 * PASO 6: SharedPreferences
 * - [ ] Guardar sesión
 * - [ ] Validar sesión
 * - [ ] Cerrar sesión
 */

