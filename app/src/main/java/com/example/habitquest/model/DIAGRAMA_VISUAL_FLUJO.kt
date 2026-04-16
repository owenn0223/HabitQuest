/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🎮 DIAGRAMA VISUAL - FLUJO COMPLETO DE HABITQUEST
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Visualización de cómo todos los componentes trabajan juntos
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 1. FLUJO AL INICIAR LA APP
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *     ┌─────────────────────────────┐
 *     │  MainActivity.onCreate()    │
 *     └────────────┬────────────────┘
 *                  │
 *                  ▼
 *     ┌─────────────────────────────┐
 *     │  SharedPreferences          │
 *     │  .getUsuarioLogueado()      │
 *     └────────────┬────────────────┘
 *                  │
 *         ┌────────┴────────┐
 *         │                 │
 *        NO                SI (ya logueado)
 *         │                 │
 *         ▼                 ▼
 *   ┌──────────────┐  ┌─────────────┐
 *   │ WELCOME      │  │ DASHBOARD   │
 *   │ SCREEN       │  │ SCREEN      │
 *   └──────────────┘  └─────────────┘
 *    [Login]              (cargando
 *    [Crear]               datos...)
 *                               │
 *                               ▼
 *                      ┌──────────────────┐
 *                      │ ViewModel loads  │
 *                      │ DashboardState   │
 *                      └──────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 2. FLUJO DE CARGA DEL DASHBOARD
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  ┌────────────────────────────────────────────────────────────────┐
 *  │                   DashboardViewModel                           │
 *  │                                                                │
 *  │  fun loadDashboardState() {                                   │
 *  │      viewModelScope.launch {                                  │
 *  │          val state = repository.getDashboardState()           │
 *  │          _dashboardState.value = state                        │
 *  │      }                                                         │
 *  │  }                                                             │
 *  └────────────────────────────────────────────────────────────────┘
 *                             │
 *                             ▼
 *  ┌────────────────────────────────────────────────────────────────┐
 *  │                   HabitRepository                              │
 *  │                                                                │
 *  │  suspend fun getDashboardState(): DashboardState {            │
 *  │      val usuario = usuarioDao.getUsuario(1)                   │
 *  │      val habitos = habitoDao.getHabitosActivos()              │
 *  │      val xpTotal = progressoDao.getSumXPTotal()               │
 *  │      val hoyCompletados = progressoDao.countHoy()             │
 *  │      val racha = calcularRacha()                              │
 *  │      val nivel = calcularNivel(xpTotal)                       │
 *  │                                                                │
 *  │      return DashboardState(                                    │
 *  │          usuario = usuario.toModel(),                         │
 *  │          habitos = habitos.map { it.toModel() },              │
 *  │          habitosCompletadosHoy = hoyCompletados,              │
 *  │          totalHabitos = habitos.size,                         │
 *  │          rachaActual = racha,                                 │
 *  │          xpTotalAcumulado = xpTotal,                          │
 *  │          nivelActual = nivel.nivel,                           │
 *  │          xpActualNivel = nivel.xpEnNivel,                     │
 *  │          xpNecesarioProximoNivel = nivel.xpParaSiguiente,     │
 *  │          proximoHabito = ...,                                 │
 *  │          porcentajeProgreso = nivel.porcentaje                │
 *  │      )                                                         │
 *  │  }                                                             │
 *  └────────────────────────────────────────────────────────────────┘
 *                     │
 *         ┌───────────┼───────────┬─────────────┐
 *         │           │           │             │
 *         ▼           ▼           ▼             ▼
 *      ┌──────┐  ┌──────┐  ┌──────────┐  ┌──────────┐
 *      │BD:   │  │BD:   │  │BD:       │  │CÁLCULOS  │
 *      │User  │  │Habit │  │Progreso  │  │- Racha   │
 *      │      │  │      │  │Diario    │  │- Nivel   │
 *      └──────┘  └──────┘  └──────────┘  │- XP      │
 *                                         └──────────┘
 *         │           │           │             │
 *         └───────────┴───────────┴─────────────┘
 *                     │
 *                     ▼
 *      ┌─────────────────────────────┐
 *      │    DashboardState           │
 *      │  (objeto con todo listo)    │
 *      └─────────────────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 3. PANTALLA DASHBOARD - CÓMO SE PINTA
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  @Composable
 *  fun DashboardScreen(
 *      viewModel: DashboardViewModel = viewModel()
 *  ) {
 *      val state by viewModel.dashboardState.collectAsState()
 *
 *      when {
 *          state == null → { LoadingScreen() }
 *          else → {
 *              Column {
 *                  Header(
 *                      titulo = "HabitQuest"  // Hardcodeado
 *                  )
 *
 *                  // USUARIO CARD
 *                  UserCard(
 *                      nombre = state.usuario.nombre
 *                      clase = state.usuario.clase
 *                      nivel = state.nivelActual
 *                      xp = "${state.xpActualNivel}/${state.xpNecesarioProximoNivel}"
 *                      progreso = state.porcentajeProgreso
 *                  )
 *
 *                  // ESTADÍSTICAS
 *                  HabitosHoy(
 *                      completados = state.habitosCompletadosHoy
 *                      total = state.totalHabitos
 *                  )
 *
 *                  CurrentStreak(
 *                      dias = state.rachaActual
 *                  )
 *
 *                  TotalXP(
 *                      xp = state.xpTotalAcumulado
 *                  )
 *
 *                  // CURRENT QUEST
 *                  if (state.proximoHabito != null) {
 *                      CurrentQuestCard(
 *                          habito = state.proximoHabito
 *                      )
 *                  }
 *
 *                  // BOTTOM NAV
 *                  BottomNavigation(
 *                      onDashboard = { },
 *                      onHabits = { navController.navigate("habits") },
 *                      onCreate = { navController.navigate("create") },
 *                      onInventory = { navController.navigate("inventory") },
 *                      onAchievements = { navController.navigate("achievements") }
 *                  )
 *              }
 *          }
 *      }
 *  }
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 4. CUANDO EL USUARIO COMPLETA UN HÁBITO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  Usuario presiona ✓ en "Morning Exercise"
 *           │
 *           ▼
 *  ┌──────────────────────────────────┐
 *  │ CreateHabitScreen.onCompleta()   │
 *  └────────┬─────────────────────────┘
 *           │
 *           ▼
 *  ┌──────────────────────────────────┐
 *  │ ViewModel.completarHabito(id=1)  │
 *  └────────┬─────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────────┐
 *  │ Repository.completarHabito(1, today)   │
 *  │                                        │
 *  │ 1. INSERT INTO progreso_diario:        │
 *  │    (habitoId=1, fecha=today,           │
 *  │     completado=true, xpGanado=40)      │
 *  │                                        │
 *  │ 2. UPDATE usuario:                     │
 *  │    SET xpTotal = xpTotal + 40          │
 *  │                                        │
 *  │ 3. Verificar logros desbloqueados      │
 *  └────────┬─────────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ ViewModel recibe notificación       │
 *  │ y recarga DashboardState            │
 *  └────────┬───────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ DashboardScreen se redibuja con:   │
 *  │ - +40 XP en total                  │
 *  │ - Nuevas estadísticas              │
 *  │ - Barra de progreso actualizada    │
 *  └────────────────────────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 5. CUANDO EL USUARIO CREA UN HÁBITO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  Usuario en CreateHabitScreen llena forma
 *           │
 *           ├─ Nombre: "Learn Kotlin"
 *           ├─ Dificultad: "HARD" (40 XP)
 *           ├─ Frecuencia: "DAILY"
 *           ├─ Tipo: "STUDY"
 *           ├─ Hora: "18:00"
 *           └─ Presiona [Crear]
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ ViewModel.crearHabito(nuevoHabito) │
 *  └────────┬───────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ Repository.crearHabito(habito)     │
 *  │                                    │
 *  │ INSERT INTO habito:                │
 *  │ VALUES (                           │
 *  │     nombre="Learn Kotlin",         │
 *  │     dificultad="HARD",             │
 *  │     frecuencia="DAILY",            │
 *  │     tipo="STUDY",                  │
 *  │     horaSugerida="18:00",          │
 *  │     estado="ACTIVE"                │
 *  │ )                                  │
 *  └────────┬───────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ ViewModel recarga DashboardState   │
 *  │ con la nueva lista de hábitos      │
 *  └────────┬───────────────────────────┘
 *           │
 *           ▼
 *  ┌────────────────────────────────────┐
 *  │ Navega a DashboardScreen           │
 *  │ que muestra el nuevo hábito        │
 *  └────────────────────────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 6. ESTRUCTURA DE CARPETAS COMPLETA
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  app/src/main/java/com/example/habitquest/
 *  │
 *  ├── model/                          ← PASO 1 (HECHO)
 *  │   ├── Usuario.kt
 *  │   ├── Habito.kt
 *  │   ├── ProgressoDiario.kt
 *  │   ├── Logro.kt
 *  │   ├── DashboardState.kt
 *  │   └── (documentación)
 *  │
 *  ├── database/                       ← PASO 2 (PRÓXIMO)
 *  │   ├── UsuarioEntity.kt
 *  │   ├── HabitoEntity.kt
 *  │   ├── ProgressoDiarioEntity.kt
 *  │   ├── LogroEntity.kt
 *  │   ├── UsuarioDao.kt
 *  │   ├── HabitoDao.kt
 *  │   ├── ProgressoDiarioDao.kt
 *  │   ├── LogroDao.kt
 *  │   └── HabitQuestDatabase.kt
 *  │
 *  ├── repository/                     ← PASO 3
 *  │   └── HabitRepository.kt
 *  │
 *  ├── viewmodel/                      ← PASO 4
 *  │   ├── DashboardViewModel.kt
 *  │   ├── CreateHabitViewModel.kt
 *  │   ├── HabitsListViewModel.kt
 *  │   └── AchievementsViewModel.kt
 *  │
 *  ├── ui/                             ← PASO 5 (ya existe)
 *  │   ├── theme/
 *  │   └── ...
 *  │
 *  ├── DashboardScreen.kt              ← MODIFICAR
 *  ├── CreateHabitScreen.kt            ← MODIFICAR
 *  ├── HabitsListScreen.kt             ← MODIFICAR
 *  ├── AchievementsScreen.kt           ← MODIFICAR
 *  ├── MainActivity.kt                 ← YA EXISTE
 *  └── LoginScreen.kt
 *
 *  preferences/                        ← PASO 6 (SessionManager)
 *  └── PreferencesManager.kt
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 7. CICLO DE VIDA DE UN REQUEST
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  USUARIO ABRE APP (PRIMER RENDER)
 *
 *  1. MainActivity crea AppNavigation()
 *  2. AppNavigation verifica sesión (SharedPreferences)
 *  3. Navega a DashboardScreen o LoginScreen
 *  4. DashboardScreen llama a ViewModel.loadData()
 *  5. ViewModel inicia corrutina que llama a Repository
 *  6. Repository hace queries a BD Room
 *  7. Repository calcula todos los valores
 *  8. ViewModel retorna DashboardState
 *  9. Compose observa el cambio y redibuja
 *  10. DashboardScreen muestra UI con datos reales
 *  11. USUARIO VE LA PANTALLA LISTA
 *
 *  ─────────────────────────────────────────
 *
 *  USUARIO COMPLETA UN HÁBITO
 *
 *  1. Usuario presiona botón ✓
 *  2. CreateHabitScreen.onClickCompleta()
 *  3. ViewModel.completarHabito(habitoId)
 *  4. Repository.completarHabito() hace INSERT + UPDATE
 *  5. ViewModel recarga DashboardState automáticamente
 *  6. Compose se redibuja con nuevos datos
 *  7. USUARIO VE CAMBIOS INMEDIATOS
 *
 *  ─────────────────────────────────────────
 *
 *  USUARIO NAVEGA A OTRA PANTALLA
 *
 *  1. Usuario presiona ícono [⚔️] en Bottom Nav
 *  2. MainActivity.AppNavigation cambia Screen
 *  3. Navega a HabitsListScreen
 *  4. HabitsListViewModel.loadHabitos()
 *  5. Repository retorna lista de hábitos
 *  6. HabitsListScreen redibuja con lista
 *  7. USUARIO VE LISTA DE HÁBITOS
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 8. COMPONENTES POR PANTALLA
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  DASHBOARD SCREEN:
 *  ├── Header
 *  ├── User Card (Usuario)
 *  ├── Stats Cards
 *  │   ├── Habits Today
 *  │   ├── Current Streak
 *  │   └── Total XP
 *  ├── Current Quest
 *  ├── Guild Locked
 *  └── Bottom Navigation ← TODAS COMPARTEN ESTO
 *
 *  HABITS LIST SCREEN:
 *  ├── Header
 *  ├── Search/Filter
 *  ├── Habit Cards (repetidas)
 *  │   ├── Nombre
 *  │   ├── Descripción
 *  │   ├── XP
 *  │   └── Checkmark (si completado hoy)
 *  ├── FAB [+ Crear]
 *  └── Bottom Navigation
 *
 *  CREATE HABIT SCREEN:
 *  ├── Header
 *  ├── Form
 *  │   ├── Nombre (TextField)
 *  │   ├── Descripción (TextField)
 *  │   ├── Dificultad (DropDown)
 *  │   ├── Frecuencia (DropDown)
 *  │   ├── Tipo (DropDown)
 *  │   ├── Hora Sugerida (TimePicker)
 *  │   └── [Crear] Button
 *  └── Back Button
 *
 *  ACHIEVEMENTS SCREEN:
 *  ├── Header
 *  ├── Tab: Desbloqueados
 *  ├── Tab: Bloqueados
 *  ├── Achievement Cards
 *  │   ├── Icono
 *  │   ├── Nombre
 *  │   ├── Descripción
 *  │   ├── Fecha (si desbloqueado)
 *  │   └── Progreso (si bloqueado)
 *  └── Bottom Navigation
 */

