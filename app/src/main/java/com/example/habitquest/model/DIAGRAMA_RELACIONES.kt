/**
 * DIAGRAMA DE RELACIONES - BASE DE DATOS HABITQUEST
 *
 * Estructura completa de datos y cómo se conectan
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * TABLA: USUARIO
 * ════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────────────────────────────────────────────────┐
 * │ USUARIO (1 solo registro por app)                            │
 * ├──────────────────────────────────────────────────────────────┤
 * │ id: Int (PRIMARY KEY) = 1                                    │
 * │ nombre: String = "Ironclad Guardian"                         │
 * │ clase: String = "WARRIOR"                                    │
 * │ nivelActual: Int = 12                                        │
 * │ xpActual: Int = 450                                          │
 * │ xpTotal: Int = 12450                                         │
 * │ rachaActual: Int = 15                                        │
 * │ ultimaFecha: String = "2024-01-16"                           │
 * │ disciplina: Int = 25                                         │
 * │ fuerza: Int = 35                                             │
 * │ inteligencia: Int = 18                                       │
 * │ consistencia: Int = 30                                       │
 * └──────────────────────────────────────────────────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * TABLA: HABITO
 * ════════════════════════════════════════════════════════════════════════
 *
 * ┌────┬──────────────────┬──────────────┬───────────┬────────────┬──────────┐
 * │ id │ nombre           │ dificultad   │ frecuencia│ xpRecompensa│ tipo     │
 * ├────┼──────────────────┼──────────────┼───────────┼────────────┼──────────┤
 * │ 1  │ Morning Exercise │ HARD         │ DAILY     │ 40         │ EXERCISE │
 * │ 2  │ Read 20 Pages    │ MEDIUM       │ DAILY     │ 20         │ STUDY    │
 * │ 3  │ Drink 8 Glasses  │ EASY         │ DAILY     │ 10         │ HEALTH   │
 * │ 4  │ Meditate         │ EASY         │ DAILY     │ 10         │ HEALTH   │
 * │ 5  │ Team Sports      │ HARD         │ WEEKLY    │ 40         │ EXERCISE │
 * │ 6  │ Learn JS         │ HARD         │ DAILY     │ 40         │ STUDY    │
 * │ 7  │ Walk 10k Steps   │ MEDIUM       │ DAILY     │ 20         │ EXERCISE │
 * └────┴──────────────────┴──────────────┴───────────┴────────────┴──────────┘
 *
 * El usuario tiene 7 hábitos activos
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * TABLA: PROGRESO_DIARIO
 * ════════════════════════════════════════════════════════════════════════
 *
 * Historial de cada día y si se completó cada hábito
 *
 * ┌────┬──────────┬────────────┬───────────┬──────────┬────────────────┐
 * │ id │ habitoId │ fecha      │ completado│ xpGanado │ horaCompletado │
 * ├────┼──────────┼────────────┼───────────┼──────────┼────────────────┤
 * │ 1  │ 1        │ 2024-01-15 │ TRUE      │ 40       │ 08:30          │
 * │ 2  │ 2        │ 2024-01-15 │ TRUE      │ 20       │ 19:45          │
 * │ 3  │ 3        │ 2024-01-15 │ FALSE     │ 0        │                │
 * │ 4  │ 4        │ 2024-01-15 │ TRUE      │ 10       │ 07:00          │
 * │ 5  │ 7        │ 2024-01-15 │ TRUE      │ 20       │ 18:00          │
 * │    │          │ (...)      │           │          │                │
 * │ 10 │ 1        │ 2024-01-16 │ TRUE      │ 40       │ 08:15          │
 * │ 11 │ 2        │ 2024-01-16 │ TRUE      │ 20       │ 20:00          │
 * │ 12 │ 3        │ 2024-01-16 │ TRUE      │ 10       │ 12:00          │
 * │ 13 │ 4        │ 2024-01-16 │ TRUE      │ 10       │ 07:15          │
 * │ 14 │ 5        │ 2024-01-16 │ FALSE     │ 0        │                │
 * │ 15 │ 6        │ 2024-01-16 │ TRUE      │ 40       │ 22:30          │
 * │ 16 │ 7        │ 2024-01-16 │ TRUE      │ 20       │ 18:30          │
 * └────┴──────────┴────────────┴───────────┴──────────┴────────────────┘
 *
 * HOY (2024-01-16):
 *   - Hábitos completados: 5 de 7 (menos el hábito 5 que es WEEKLY)
 *   - XP ganado hoy: 140 (40+20+10+10+40+20)
 *
 * RACHA:
 *   - 2024-01-16: 6 de 7 completados (falta hábito 5 WEEKLY)
 *   - 2024-01-15: 4 de 6 completados (hábito 3 no completado)
 *   - ¿Racha = 0 o 2?
 *
 *   Depende de la LÓGICA que definamos:
 *
 *   OPCIÓN A (Estricta - requiere 100%):
 *     Si alguien no completó 1 hábito, racha = 0
 *     Racha actual = 0 (porque ayer falló)
 *
 *   OPCIÓN B (Flexible - permite fallar 1):
 *     Si completó 80%+ del día, cuenta como completo
 *     Racha actual = 2
 *
 *   OPCIÓN C (Solo DAILY):
 *     Solo contar hábitos DAILY, ignorar WEEKLY
 *     2024-01-16: 5 de 6 completados (falta hábito 3)
 *     2024-01-15: 4 de 5 completados (falta hábito 3)
 *     Racha = 0 (porque ambos días tuvieron fallos)
 *
 * Yo recomiendo OPCIÓN C (es la más común en apps de hábitos)
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * TABLA: LOGRO
 * ════════════════════════════════════════════════════════════════════════
 *
 * ┌────┬─────────────────┬──────────────────────┬──────────┬────────────┐
 * │ id │ nombre          │ condicion            │ valor    │ desbloqueado│
 * ├────┼─────────────────┼──────────────────────┼──────────┼────────────┤
 * │ 1  │ Getting Started │ HABITS_COMPLETED     │ 1        │ TRUE       │
 * │ 2  │ Week Warrior    │ STREAK               │ 7        │ TRUE       │
 * │ 3  │ XP Collector    │ TOTAL_XP             │ 500      │ TRUE       │
 * │ 4  │ Rising Hero     │ LEVEL                │ 10       │ TRUE       │
 * │ 5  │ Legend Status   │ STREAK               │ 30       │ FALSE      │
 * │ 6  │ XP Millionaire  │ TOTAL_XP             │ 10000    │ FALSE      │
 * │ 7  │ Master Habits   │ HABITS_COMPLETED     │ 100      │ FALSE      │
 * └────┴─────────────────┴──────────────────────┴──────────┴────────────┘
 *
 * El usuario ha desbloqueado 4 logros
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * CÁLCULOS QUE USAREMOS EN EL DASHBOARD
 * ════════════════════════════════════════════════════════════════════════
 *
 * 1. HÁBITOS HOY (4/8):
 *    SELECT COUNT(*)
 *    FROM progreso_diario
 *    WHERE fecha = TODAY AND completado = TRUE
 *    RESULTADO: 5
 *
 *    (Mostramos: 5 de 7 activos, no contamos los ARCHIVED)
 *
 * ---
 *
 * 2. RACHA ACTUAL (15 días):
 *    - Verificar cada día hacia atrás
 *    - Contar días DAILY completados al 100%
 *    - Detener en primer día incompleto
 *
 *    Pseudocódigo:
 *    racha = 0
 *    fechaActual = HOY
 *    while true:
 *        habitosDiarios = SELECT FROM habito WHERE frecuencia = 'DAILY' AND estado = 'ACTIVE'
 *        completados = SELECT COUNT FROM progreso_diario
 *                      WHERE fecha = fechaActual AND completado = TRUE
 *                      AND habitoId IN habitosDiarios
 *
 *        if completados == totalHabitosDiarios:
 *            racha++
 *            fechaActual = fechaActual - 1 día
 *        else:
 *            break
 *
 * ---
 *
 * 3. XP TOTAL ACUMULADO (12,450):
 *    SELECT SUM(xpGanado)
 *    FROM progreso_diario
 *    WHERE completado = TRUE
 *    RESULTADO: 12450
 *
 * ---
 *
 * 4. NIVEL ACTUAL (12) y PROGRESO (450/1000):
 *
 *    FÓRMULA:
 *    XP_para_nivel_N = N * 100
 *
 *    Ejemplo con 12,450 XP total:
 *    - Nivel 1: 0 a 99 XP → subió
 *    - Nivel 2: 100 a 199 XP → subió
 *    - ... (sumar hasta 12 niveles)
 *    - Nivel 12: 1100 a 1199 XP → EN PROGRESO
 *    - XP actual en nivel 12: 450 XP de 1000 necesarios
 *
 *    Algoritmo:
 *    nivelActual = 1
 *    xpAcumuladoAntesDeNivel = 0
 *
 *    for nivel = 1 to infinito:
 *        xpNecesarioEsteNivel = nivel * 100
 *        xpAcumuladoAntesDeNivel += xpNecesarioAntesDeNivel
 *
 *        if xpTotal < xpAcumuladoAntesDeNivel + xpNecesarioEsteNivel:
 *            nivelActual = nivel
 *            xpActualEnNivel = xpTotal - xpAcumuladoAntesDeNivel
 *            break
 *
 * ---
 *
 * 5. PRÓXIMO HÁBITO (Current Quest):
 *    SELECT * FROM habito
 *    WHERE estado = 'ACTIVE'
 *    ORDER BY horaSugerida ASC
 *    LIMIT 1
 *
 *    (El próximo a la hora sugerida, o el primero incompleto hoy)
 *
 * ---
 *
 * 6. PORCENTAJE BARRA DE PROGRESO:
 *    porcentaje = xpActualEnNivel / (nivelActual * 100)
 *    ejemplo: 450 / 1200 = 0.375 = 37.5%
 */

/**
 * ════════════════════════════════════════════════════════════════════════
 * FLUJO DE DATOS EN LA APLICACIÓN
 * ════════════════════════════════════════════════════════════════════════
 *
 * 1. USUARIO ABRE LA APP:
 *    MainActivity → AppNavigation → determina si fue logueado
 *    ✓ Logueado → DashboardScreen
 *    ✗ No logueado → WelcomeScreen
 *
 * 2. USUARIO LLEGA AL DASHBOARD:
 *    DashboardViewModel.loadDashboardData()
 *    │
 *    ├─→ Repository.getUsuario()
 *    │   └─→ Database.usuarioDao().getUsuario(id=1)
 *    │
 *    ├─→ Repository.getHabitosActivos()
 *    │   └─→ Database.habitoDao().getHabitosActivos()
 *    │
 *    ├─→ Repository.getHabitosCompletadosHoy()
 *    │   └─→ Query: COUNT progreso_diario WHERE fecha = HOY
 *    │
 *    ├─→ Repository.calcularRacha()
 *    │   └─→ Query: verificar últimos N días
 *    │
 *    └─→ Repository.calcularNivelYXP()
 *        └─→ Query: SUM xpGanado desde inicio
 *
 *    Retorna: DashboardState (objeto único con todo)
 *    │
 *    └─→ DashboardScreen recibe DashboardState
 *        └─→ Pinta UI con los datos
 *
 * 3. USUARIO COMPLETA UN HÁBITO:
 *    Usuario presiona "✓" en un hábito
 *    │
 *    ├─→ Repository.completarHabito(habitoId, fecha)
 *    │   └─→ INSERT INTO progreso_diario (habitoId, fecha, completado, xpGanado)
 *    │       VALUES (habitoId, TODAY, TRUE, xpRecompensa)
 *    │
 *    ├─→ Usuario gana XP
 *    │   └─→ UPDATE usuario SET xpTotal = xpTotal + xpRecompensa
 *    │
 *    └─→ Actualizar DashboardState
 *        └─→ UI se redibuja automáticamente
 *
 * 4. USUARIO CREA UN NUEVO HÁBITO:
 *    CreateHabitScreen → presiona "Crear"
 *    │
 *    ├─→ Repository.crearHabito(habito)
 *    │   └─→ INSERT INTO habito (nombre, tipo, etc)
 *    │       VALUES (...)
 *    │
 *    └─→ Navegar a DashboardScreen
 *        └─→ Se recarga la lista de hábitos
 */