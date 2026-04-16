package com.example.habitquest.model

import java.io.Serializable

/**
 * Modelo que AGRUPA toda la información que necesita el Dashboard
 *
 * Es como una "vista" de base de datos que combina:
 * - Datos del usuario
 * - Hábitos
 * - Estadísticas calculadas
 *
 * Esto es útil porque la UI no necesita hacer cálculos complejos,
 * solo mostrar los datos que recibe.
 */

data class DashboardState(
    val usuario: Usuario,
    val habitos: List<Habito> = emptyList(),
    val habitosCompletadosHoy: Int = 0,
    val totalHabitos: Int = 0,
    val rachaActual: Int = 0,
    val xpTotalAcumulado: Int = 0,
    val xpActualNivel: Int = 0,
    val xpNecesarioProximoNivel: Int = 0,
    val nivelActual: Int = 1,
    val proximoHabito: Habito? = null, // El próximo a completar
    val porcentajeProgreso: Float = 0f // 0.0 a 1.0 para la barra
) : Serializable

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿Por qué un estado agregado?
 *
 * PATRÓN ARQUITECTÓNICO: MVVM + State
 *
 * En lugar de que el Dashboard haga múltiples queries a la BD:
 *
 * ❌ MAL (no escalable):
 * - Query 1: Obtener usuario
 * - Query 2: Obtener hábitos
 * - Query 3: Contar completados hoy
 * - Query 4: Calcular racha
 * - Query 5: Sumar XP total
 * ... (muchas queries innecesarias)
 *
 * ✅ BIEN (eficiente):
 * - ViewModelRepository hace TODAS las queries necesarias
 * - Retorna 1 objeto DashboardState con todo
 * - La UI solo muestra los datos
 *
 * BENEFICIOS:
 * 1. Performance: Menos queries a la BD
 * 2. Testeable: Fácil de mockear para pruebas
 * 3. Mantenible: La lógica está centralizada
 * 4. Observable: Con Flow/LiveData se actualiza automáticamente
 *
 * ---
 *
 * CÁLCULOS QUE AQUÍ SE HACEN:
 *
 * xpNecesarioProximoNivel:
 *   - Fórmula: nivelActual * 100
 *   - Ejemplo: Nivel 12 → necesita 1200 XP para nivel 13
 *
 * porcentajeProgreso:
 *   - xpActualNivel / xpNecesarioProximoNivel
 *   - Ejemplo: 450 / 1000 = 0.45 (45% de barra)
 *
 * rachaActual:
 *   - Se calcula verificando ProgressoDiario día a día
 *   - Si HOY no completó todos → racha = 0
 *   - Si ayer no completó todos → racha = 0
 *   - Sino, contar hacia atrás hasta encontrar un día incompleto
 *
 * habitosCompletadosHoy:
 *   - COUNT de ProgressoDiario donde fecha = HOY y completado = TRUE
 */

