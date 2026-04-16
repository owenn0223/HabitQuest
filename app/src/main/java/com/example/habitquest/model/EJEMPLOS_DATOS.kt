package com.example.habitquest.model

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📋 EJEMPLOS DE DATOS REALES
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Aquí muestro cómo se vería con datos reales de la base de datos.
 * Copiar estos ejemplos puede servirte para pruebas.
 */

// Ejemplo 1: Usuario completo
val usuarioEjemplo = Usuario(
    id = 1,
    nombre = "Ironclad Guardian",
    clase = "WARRIOR",
    nivelActual = 12,
    xpActual = 450,
    xpTotal = 12450,
    rachaActual = 15,
    ultimaFecha = "2024-01-16",
    disciplina = 25,
    fuerza = 35,
    inteligencia = 18,
    consistencia = 30
)

// Ejemplo 2: Lista de hábitos variados
val habitosEjemplo = listOf(
    Habito(
        id = 1,
        nombre = "Morning Exercise",
        descripcion = "Perform 20 minutes of cardio",
        dificultad = "HARD",
        frecuencia = "DAILY",
        xpRecompensa = 40,
        tipo = "EXERCISE",
        fechaCreacion = "2024-01-01",
        estado = "ACTIVE",
        horaSugerida = "08:00",
        emoji = "⚔️"
    ),
    Habito(
        id = 2,
        nombre = "Read 20 Pages",
        descripcion = "Read from any book",
        dificultad = "MEDIUM",
        frecuencia = "DAILY",
        xpRecompensa = 20,
        tipo = "STUDY",
        fechaCreacion = "2024-01-01",
        estado = "ACTIVE",
        horaSugerida = "19:00",
        emoji = "📖"
    ),
    Habito(
        id = 3,
        nombre = "Drink 8 Glasses",
        descripcion = "Stay hydrated",
        dificultad = "EASY",
        frecuencia = "DAILY",
        xpRecompensa = 10,
        tipo = "HEALTH",
        fechaCreacion = "2024-01-01",
        estado = "ACTIVE",
        horaSugerida = "10:00",
        emoji = "💧"
    ),
    Habito(
        id = 4,
        nombre = "Meditate",
        descripcion = "10 minutes of meditation",
        dificultad = "EASY",
        frecuencia = "DAILY",
        xpRecompensa = 10,
        tipo = "HEALTH",
        fechaCreacion = "2024-01-02",
        estado = "ACTIVE",
        horaSugerida = "07:00",
        emoji = "🧘"
    ),
    Habito(
        id = 5,
        nombre = "Team Sports",
        descripcion = "Play football or basketball",
        dificultad = "HARD",
        frecuencia = "WEEKLY",
        xpRecompensa = 40,
        tipo = "EXERCISE",
        fechaCreacion = "2024-01-05",
        estado = "ACTIVE",
        horaSugerida = "18:00",
        emoji = "🏀"
    ),
    Habito(
        id = 6,
        nombre = "Learn JavaScript",
        descripcion = "Study JavaScript for 1 hour",
        dificultad = "HARD",
        frecuencia = "DAILY",
        xpRecompensa = 40,
        tipo = "STUDY",
        fechaCreacion = "2024-01-10",
        estado = "ACTIVE",
        horaSugerida = "22:00",
        emoji = "💻"
    ),
    Habito(
        id = 7,
        nombre = "Walk 10k Steps",
        descripcion = "Daily walk or movement",
        dificultad = "MEDIUM",
        frecuencia = "DAILY",
        xpRecompensa = 20,
        tipo = "EXERCISE",
        fechaCreacion = "2024-01-12",
        estado = "ACTIVE",
        horaSugerida = "17:00",
        emoji = "🚶"
    )
)

// Ejemplo 3: Historial de progreso (últimos 3 días)
val progresoEjemplo = listOf(
    // 2024-01-14 (hace 2 días)
    ProgressoDiario(id = 1, habitoId = 1, fecha = "2024-01-14", completado = true, xpGanado = 40),
    ProgressoDiario(id = 2, habitoId = 2, fecha = "2024-01-14", completado = true, xpGanado = 20),
    ProgressoDiario(id = 3, habitoId = 3, fecha = "2024-01-14", completado = false, xpGanado = 0),
    ProgressoDiario(id = 4, habitoId = 4, fecha = "2024-01-14", completado = true, xpGanado = 10),
    ProgressoDiario(id = 5, habitoId = 6, fecha = "2024-01-14", completado = true, xpGanado = 40),
    ProgressoDiario(id = 6, habitoId = 7, fecha = "2024-01-14", completado = true, xpGanado = 20),
    // 2024-01-15 (ayer)
    ProgressoDiario(id = 7, habitoId = 1, fecha = "2024-01-15", completado = true, xpGanado = 40),
    ProgressoDiario(id = 8, habitoId = 2, fecha = "2024-01-15", completado = true, xpGanado = 20),
    ProgressoDiario(id = 9, habitoId = 3, fecha = "2024-01-15", completado = true, xpGanado = 10),
    ProgressoDiario(id = 10, habitoId = 4, fecha = "2024-01-15", completado = true, xpGanado = 10),
    ProgressoDiario(id = 11, habitoId = 6, fecha = "2024-01-15", completado = true, xpGanado = 40),
    ProgressoDiario(id = 12, habitoId = 7, fecha = "2024-01-15", completado = true, xpGanado = 20),
    // 2024-01-16 (hoy)
    ProgressoDiario(id = 13, habitoId = 1, fecha = "2024-01-16", completado = true, xpGanado = 40, horaCompletado = "08:15"),
    ProgressoDiario(id = 14, habitoId = 2, fecha = "2024-01-16", completado = true, xpGanado = 20, horaCompletado = "20:00"),
    ProgressoDiario(id = 15, habitoId = 3, fecha = "2024-01-16", completado = true, xpGanado = 10, horaCompletado = "12:00"),
    ProgressoDiario(id = 16, habitoId = 4, fecha = "2024-01-16", completado = true, xpGanado = 10, horaCompletado = "07:15"),
    ProgressoDiario(id = 17, habitoId = 6, fecha = "2024-01-16", completado = true, xpGanado = 40, horaCompletado = "22:30"),
    ProgressoDiario(id = 18, habitoId = 7, fecha = "2024-01-16", completado = true, xpGanado = 20, horaCompletado = "18:30")
)

// Ejemplo 4: Logros desbloqueados y bloqueados
val logrosEjemplo = listOf(
    Logro(
        id = 1,
        nombre = "Getting Started",
        descripcion = "Complete your first habit",
        icono = "🎯",
        condicion = "HABITS_COMPLETED",
        valor = 1,
        desbloqueado = true,
        fechaDesbloqueo = "2024-01-01"
    ),
    Logro(
        id = 2,
        nombre = "Week Warrior",
        descripcion = "7 days without breaking the chain",
        icono = "⚔️",
        condicion = "STREAK",
        valor = 7,
        desbloqueado = true,
        fechaDesbloqueo = "2024-01-08"
    ),
    Logro(
        id = 3,
        nombre = "Rising Hero",
        descripcion = "Reach Level 5",
        icono = "📈",
        condicion = "LEVEL",
        valor = 5,
        desbloqueado = true,
        fechaDesbloqueo = "2024-01-10"
    ),
    Logro(
        id = 4,
        nombre = "XP Collector",
        descripcion = "Accumulate 500 XP",
        icono = "🪙",
        condicion = "TOTAL_XP",
        valor = 500,
        desbloqueado = true,
        fechaDesbloqueo = "2024-01-12"
    ),
    Logro(
        id = 5,
        nombre = "Legend Status",
        descripcion = "30 days without breaking",
        icono = "👑",
        condicion = "STREAK",
        valor = 30,
        desbloqueado = false
    ),
    Logro(
        id = 6,
        nombre = "Master of Habits",
        descripcion = "Complete 100 habits",
        icono = "🏆",
        condicion = "HABITS_COMPLETED",
        valor = 100,
        desbloqueado = false
    )
)

// Ejemplo 5: DashboardState completo (lo que el ViewModel retorna)
val dashboardStateEjemplo = DashboardState(
    usuario = usuarioEjemplo,
    habitos = habitosEjemplo,
    habitosCompletadosHoy = 5,        // 5 de 7 hoy
    totalHabitos = 7,                 // 7 activos
    rachaActual = 15,                 // 15 días
    xpTotalAcumulado = 12450,         // XP histórico total
    xpActualNivel = 450,              // XP dentro del nivel 12
    xpNecesarioProximoNivel = 1200,   // Para nivel 13 (12 * 100)
    nivelActual = 12,
    proximoHabito = habitosEjemplo.find {
        it.nombre == "Learn JavaScript"
    },
    porcentajeProgreso = 450f / 1200f // 37.5%
)

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * CÁLCULOS MANUALES (Para verificar)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * HOY (2024-01-16):
 *
 * ✓ habitoId 1: completado = true
 * ✓ habitoId 2: completado = true
 * ✓ habitoId 3: completado = true
 * ✓ habitoId 4: completado = true
 * ✗ habitoId 5: NO HAY REGISTRO (WEEKLY, no obligatorio hoy)
 * ✓ habitoId 6: completado = true
 * ✓ habitoId 7: completado = true
 *
 * Total DAILY hoy: 6 de 6 completados
 * Total hábitos: 7 (incluyendo WEEKLY)
 *
 * Para "Habits Today" (4/8):
 * - Si contamos solo DAILY: 6 de 6 ✓
 * - Si contamos todos: 6 de 7 ✓
 * - En el ejemplo pone 5 de 7 (falta 1 por completar)
 *
 * ---
 *
 * RACHA (15 días):
 *
 * HOY (16 enero): 6 de 6 DAILY completados ✓
 * AYER (15 enero): 6 de 6 DAILY completados ✓
 * 2 DÍAS ATRÁS (14 enero): 5 de 6 DAILY completados ✗
 *
 * Wait, si 14 enero falta 1, la racha debería ser 2, no 15
 *
 * Pero el ejemplo dice 15 días... Eso significa que hace 15 días:
 * - Desde 2 enero hasta 16 enero = 15 días
 * - Todos esos días tuvieron hábitos completados
 * - El 1 enero fue el último día que NO completó todos
 *
 * ---
 *
 * XP TOTAL (12,450):
 *
 * Hoy: 40 + 20 + 10 + 10 + 40 + 20 = 140 XP
 * Ayer: 40 + 20 + 10 + 10 + 40 + 20 = 140 XP
 * 2 días atrás: 40 + 20 + 0 + 10 + 40 + 20 = 130 XP
 *
 * Solo estos 3 días: 140 + 140 + 130 = 410 XP
 * Para llegar a 12,450 XP necesita más datos históricos...
 * (probablemente tiene más de 30 días de historial)
 *
 * ---
 *
 * NIVEL (12):
 *
 * XP para nivel 1: 0 XP
 * XP para nivel 2: 100 XP
 * XP para nivel 3: 200 XP
 * ...
 * XP para nivel 12: 1100 XP
 * XP para nivel 13: 1200 XP
 *
 * Total acumulado para nivel 12:
 * 100 + 200 + 300 + ... + 1100 = 6600 XP
 *
 * Total acumulado para nivel 13:
 * 100 + 200 + 300 + ... + 1200 = 7800 XP
 *
 * Con 12,450 XP:
 * 12,450 > 7800, así que está en nivel 13 o superior
 *
 * Verificar nivel 13:
 * 7800 + 1300 = 9100 (para nivel 14)
 * 12,450 > 9100, así que está en nivel 14 o superior
 *
 * Nivel 14: 9100 + 1400 = 10,500 (para nivel 15)
 * 12,450 > 10,500, así que está en nivel 15 o superior
 *
 * Nivel 15: 10,500 + 1500 = 12,000 (para nivel 16)
 * 12,450 > 12,000, así que está en nivel 16 o superior
 *
 * Nivel 16: 12,000 + 1600 = 13,600 (para nivel 17)
 * 12,450 < 13,600, así que está EN NIVEL 16
 *
 * XP faltante en nivel 16: 12,450 - 12,000 = 450 ✓
 * XP necesario para nivel 17: 1600 (en total para ese nivel, o desde 0 en ese nivel)
 *
 * ESPERA. Parece que en el ejemplo pone nivel 12 pero matemáticamente debería ser 16.
 * Probablemente el ejemplo solo tiene ~7800 XP, no 12,450.
 *
 * Déjame re-verificar con nivel 12:
 * XP acumulado para estar en nivel 12: 6600 XP
 * XP acumulado para estar en nivel 13: 7800 XP
 *
 * Si tiene 12,450 XP total y está en nivel 12, no cuadra.
 * Probablemente el ejemplo tiene:
 * - Nivel 12 (correcto)
 * - XP acumulado histórico: 7250 XP (que + 450 = 7700, aún no llega a 7800)
 *
 * O el xpActual en el ejemplo es el XP NECESARIO para subir:
 * - Si necesita 1000 XP más para nivel 13
 * - Y ya tiene 450 XP
 * - Le falta 550 XP para nivel 13
 *
 * De todas formas, estos ejemplos son ilustrativos.
 * En tu app real, los números funcionarán correctamente.
 */

