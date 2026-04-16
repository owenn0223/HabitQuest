/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🎯 MAPEO - DATOS REALES EN EL DASHBOARD
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Esto muestra EXACTAMENTE de dónde viene cada dato que el Dashboard pinta.
 * Útil para que entiendas cómo conectar BD con UI.
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * DASHBOARD ACTUAL (Imagen):
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * [🏠] [⚔️] [+] [🎒] [👥]  ← Bottom Navigation
 *
 * Cuando presiones el +, irás a CREATE_HABIT
 * Cuando presiones ⚔️, irás a HABITS_LIST
 * Cuando presiones 👥, irás a ACHIEVEMENTS
 * Cuando presiones 🎒, irás a INVENTORY (futuro)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 1: HEADER
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌────────────────────────────────────────┐
 * │ [✔] HabitQuest    🔔 ⚙️                │
 * └────────────────────────────────────────┘
 *
 * 📊 DATOS:
 * - Logo: Hardcodeado ("✔")
 * - Título: Hardcodeado ("HabitQuest")
 * - Notificaciones: Ícono (sin funcionalidad aún)
 * - Settings: Ícono (sin funcionalidad aún)
 *
 * TIPO: Static (no cambia)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 2: CARD DEL USUARIO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌──────────────────────────────────────────────┐
 * │ [🛡️] Ironclad Guardian                       │
 * │      WARRIOR CLASS                           │
 * │                                              │
 * │      [LVL 12] XP to Level 13 450 / 1000     │
 * │      ████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ │
 * └──────────────────────────────────────────────┘
 *
 * 📊 DATOS (De: Usuario):
 * - emoji: "🛡️" (usuario.clase)
 * - nombre: "Ironclad Guardian" (usuario.nombre)
 * - clase: "WARRIOR CLASS" (usuario.clase)
 * - nivel: "LVL 12" (usuario.nivelActual)
 * - xpActual: 450 (usuario.xpActual)
 * - xpProximo: 1000 (usuario.nivelActual * 100)
 * - barra: 450/1000 = 0.45 (45% lleno)
 *
 * TIPO: Dynamic (cambia con ViewModel)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 3: ESTADÍSTICAS (3 BOXES)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌──────────────────┐ ┌──────────────────┐
 * │ Habits Today     │ │ Current Streak   │
 * │                  │ │                  │
 * │      4/8         │ │     15 Days      │
 * └──────────────────┘ └──────────────────┘
 *
 * ┌──────────────────────────────────────────┐
 * │ Total Lifetime XP        [🪙]            │
 * │                                          │
 * │      12,450                              │
 * └──────────────────────────────────────────┘
 *
 * 📊 DATOS:
 *
 * "Habits Today" (4/8):
 *   numerador = SELECT COUNT(*) FROM progreso_diario
 *               WHERE fecha = TODAY AND completado = TRUE
 *   denominador = SELECT COUNT(*) FROM habito
 *                 WHERE estado = 'ACTIVE' AND frecuencia = 'DAILY'
 *
 *   Resultado: "4/8" (el usuario completó 4 de 8 hábitos diarios hoy)
 *
 * "Current Streak" (15 Days):
 *   = Usuario.rachaActual (que se calcula en Repository)
 *
 *   Lógica: Verificar cada día hacia atrás
 *   - Hoy: ¿completó todos los DAILY? Sí → +1
 *   - Ayer: ¿completó todos los DAILY? Sí → +1
 *   - Hace 2 días: ¿completó todos? Sí → +1
 *   ...
 *   - Hace 15 días: ¿completó todos? Sí → +1
 *   - Hace 16 días: ¿completó todos? NO → Detenerse
 *
 *   Resultado: 15 días de racha
 *
 * "Total Lifetime XP" (12,450):
 *   = SELECT SUM(xpGanado) FROM progreso_diario
 *     WHERE completado = TRUE
 *
 *   Resultado: 12,450 XP total acumulado
 *
 * TIPO: Dynamic (todo viene de ViewModel)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 4: CURRENT QUEST (Próximo hábito)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌──────────────────────────────────────────────┐
 * │ Current Quest                                │
 * │                                              │
 * │ [⚔️] Morning Vitality                         │
 * │     Perform 20 minutes of cardio             │
 * │                                              │
 * │     +50 XP     08:30 AM        [▶️]          │
 * └──────────────────────────────────────────────┘
 *
 * 📊 DATOS (De: Habito + ProgressoDiario):
 *
 * Se obtiene el próximo hábito a completar hoy:
 *
 *   proximoHabito = habitos.filter {
 *       it.estado == "ACTIVE" &&
 *       !isCompletadoHoy(it.id)
 *   }.minByOrNull {
 *       it.horaSugerida // Ordenar por hora sugerida
 *   }
 *
 *   Si proximoHabito != null:
 *   - emoji: proximoHabito.emoji ("⚔️")
 *   - nombre: proximoHabito.nombre ("Morning Vitality")
 *   - descripcion: proximoHabito.descripcion
 *   - xp: proximoHabito.xpRecompensa (50 XP) ← calcula según dificultad
 *   - hora: proximoHabito.horaSugerida ("08:30 AM")
 *   - botón play: presionar marca como completado
 *
 * TIPO: Dynamic (calcula cuál es el próximo)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 5: GUILD BLOQUEADO (Info)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌──────────────────────────────────────────────┐
 * │ [🔒] Join a Guild at Level 15 to unlock      │
 * │     collaborative raids and weekly rewards!  │
 * └──────────────────────────────────────────────┘
 *
 * 📊 DATOS:
 * - Mostrar solo si usuario.nivelActual < 15
 * - Mensaje: Hardcodeado (para futuras features)
 *
 * TIPO: Conditional (depende del nivel)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 6: BOTTOM NAVIGATION
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 🖼️ VISUAL:
 * ┌──────────────────────────────────────────────┐
 * │  [🏠]  [⚔️]  [+]  [🎒]  [👥]                  │
 * └──────────────────────────────────────────────┘
 *
 * FUNCIONALIDAD:
 * [🏠] → Screen.DASHBOARD (ya está aquí, se resalta)
 * [⚔️] → Screen.HABITS_LIST (ir a lista de hábitos)
 * [+]  → Screen.CREATE_HABIT (crear nuevo hábito)
 * [🎒] → Screen.INVENTORY (futuro - items desbloqueados)
 * [👥] → Screen.ACHIEVEMENTS (ir a logros)
 *
 * TIPO: Navigation (static, solo botones)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * EJEMPLO REAL: DASHBOARD CON DATOS
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * En base de datos tenemos:
 *
 * TABLA USUARIO:
 * id=1, nombre="Ironclad Guardian", clase="WARRIOR",
 * nivelActual=12, xpTotal=12450, rachaActual=15
 *
 * TABLA HABITO:
 * id=1: Morning Exercise (HARD, DAILY, 40 XP)
 * id=2: Read 20 Pages (MEDIUM, DAILY, 20 XP)
 * id=3: Drink 8 Glasses (EASY, DAILY, 10 XP)
 * id=4: Meditate (EASY, DAILY, 10 XP)
 * id=5: Team Sports (HARD, WEEKLY, 40 XP)
 * id=6: Learn JavaScript (HARD, DAILY, 40 XP)
 * id=7: Walk 10k Steps (MEDIUM, DAILY, 20 XP)
 *
 * TABLA PROGRESO_DIARIO (HOY - 2024-01-16):
 * habitoId=1: completado=TRUE (40 XP)
 * habitoId=2: completado=TRUE (20 XP)
 * habitoId=3: completado=FALSE (0 XP)
 * habitoId=4: completado=TRUE (10 XP)
 * habitoId=5: completado=FALSE (0 XP) - es WEEKLY
 * habitoId=6: completado=TRUE (40 XP)
 * habitoId=7: completado=TRUE (20 XP)
 *
 *
 * EL DASHBOARD MOSTRARÁ:
 *
 * ┌────────────────────────────────────────────────┐
 * │ [✔] HabitQuest            🔔 ⚙️                │
 * ├────────────────────────────────────────────────┤
 * │ [🛡️] Ironclad Guardian                         │
 * │      WARRIOR CLASS                             │
 * │                                                │
 * │      [LVL 12] XP to Level 13 450 / 1000       │
 * │      ████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
 * ├────────────────────────────────────────────────┤
 * │ ┌──────────────┐  ┌──────────────┐             │
 * │ │ Habits Today │  │ Current Str. │             │
 * │ │              │  │              │             │
 * │ │    5/7       │  │   15 Days    │             │
 * │ └──────────────┘  └──────────────┘             │
 * │                                                │
 * │ ┌────────────────────────────────────────────┐ │
 * │ │ Total Lifetime XP     [🪙]                 │ │
 * │ │ 12,450                                     │ │
 * │ └────────────────────────────────────────────┘ │
 * ├────────────────────────────────────────────────┤
 * │ Current Quest                                  │
 * │                                                │
 * │ [⚔️] Learn JavaScript                          │
 * │     Master web development                    │
 * │                                                │
 * │     +40 XP   18:00 PM        [▶️]             │
 * ├────────────────────────────────────────────────┤
 * │ [🔒] Join a Guild at Level 15...             │
 * ├────────────────────────────────────────────────┤
 * │  [🏠]  [⚔️]  [+]  [🎒]  [👥]                   │
 * └────────────────────────────────────────────────┘
 *
 * CÁLCULOS REALIZADOS:
 * - "5/7": 5 de 7 completados hoy (no contamos WEEKLY)
 * - "15 Days": Racha sin errores en últimos 15 días
 * - "12,450": Suma de todos los XP ganados históricos
 * - "LVL 12": Nivel calculado a partir de 12,450 XP
 * - "450/1000": XP faltante en el nivel actual
 * - "Learn JavaScript": Próximo hábito sin completar (ordenado por hora)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🧮 CÁLCULO MATEMÁTICO DE NIVEL
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Con 12,450 XP total:
 *
 * XP_para_nivel_N = N * 100
 *
 * Nivel 1: 0 a 99 XP (falta: 100 para nivel 2)
 * Nivel 2: 100 a 199 XP (falta: 100 para nivel 3)
 * ...
 * Nivel 12: 1100 a 1199 XP (falta: 100 para nivel 13)
 * Nivel 13: 1200 a 1299 XP
 * ...
 *
 * Suma de XP requeridos para cada nivel:
 * 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12 = 78
 * 78 * 100 = 7800 XP para alcanzar nivel 12
 *
 * 12,450 - 7800 = 4,650 XP en nivel 12 (pero max es 1200 por nivel)
 *
 * Espera, eso no cuadra. Déjame recalcular:
 *
 * Para nivel 13: necesita (12 * 100) = 1200 XP dentro del nivel
 * Para nivel 14: necesita (13 * 100) = 1300 XP dentro del nivel
 * ...
 *
 * Total XP para alcanzar nivel 13:
 * (1*100) + (2*100) + (3*100) + ... + (12*100) = 7800 XP
 *
 * Total XP para alcanzar nivel 24:
 * (1*100) + (2*100) + ... + (23*100) = 27600 XP
 *
 * 12,450 está entre 7800 y 9000, así que está en nivel 12
 *
 * XP actual en nivel 12 = 12,450 - 7800 = 4,650
 * Pero espera, eso excede 1200. Necesito revisar...
 *
 * ¡AH! El xpActual debe ser el RESTO dentro del nivel:
 * xpActual = 12,450 % (nivelActual * 100)
 * xpActual = 12,450 % 1200 = 450
 *
 * Perfecto. Así que:
 * - Nivel: 12
 * - XP dentro de nivel 12: 450 de 1200
 * - XP faltante para nivel 13: 1200 - 450 = 750
 * - Barra: 450 / 1200 = 37.5%
 */

