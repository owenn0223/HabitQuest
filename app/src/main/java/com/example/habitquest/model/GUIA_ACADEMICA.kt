/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📚 GUÍA ACADÉMICA - ESTRUCTURA DEL PROYECTO HABITQUEST
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Este documento explica la arquitectura completa y requisitos académicos
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 1: MODELOS DE DATOS (Aquí estamos)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Archivos creados:
 *
 * ✅ Usuario.kt
 *    - Datos del personaje RPG
 *    - Nivel, XP, estadísticas
 *    - Racha actual
 *
 * ✅ Habito.kt
 *    - Nombre, descripción
 *    - Dificultad (EASY, MEDIUM, HARD)
 *    - Frecuencia (DAILY, WEEKLY)
 *    - Tipo para estadísticas
 *
 * ✅ ProgressoDiario.kt
 *    - Historial de completados/no completados
 *    - CRUCIAL: Aquí se guardan todos los datos históricos
 *    - Permite calcular racha, XP total, tendencias
 *
 * ✅ Logro.kt
 *    - Achievements desbloqueables
 *    - Motivación gamificada
 *
 * ✅ DashboardState.kt
 *    - Estado agregado (MVVM pattern)
 *    - Una sola fuente de verdad para el Dashboard
 *
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 2: BASE DE DATOS (Room) - PRÓXIMO PASO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Necesitaremos crear:
 *
 * 📁 database/
 *    ├── UsuarioEntity.kt (con @Entity)
 *    ├── HabitoEntity.kt
 *    ├── ProgressoDiarioEntity.kt
 *    ├── LogroEntity.kt
 *    ├── UsuarioDao.kt (métodos para CRUD)
 *    ├── HabitoDao.kt
 *    ├── ProgressoDiarioDao.kt
 *    ├── LogroDao.kt
 *    └── HabitQuestDatabase.kt (la base de datos)
 *
 * Requisito académico: "Persistencia local con Room"
 *
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 3: REPOSITORIO - LÓGICA DE NEGOCIO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Crear:
 *
 * 📁 repository/
 *    └── HabitRepository.kt
 *
 *       Métodos principales:
 *       - getUsuario()
 *       - crearHabito()
 *       - completarHabito()
 *       - calcularRacha()
 *       - calcularNivel()
 *       - obtenerDashboardState()
 *
 * Aquí es donde TODA la lógica vive:
 * - Cálculo de racha
 * - Cálculo de niveles
 * - Cálculo de estadísticas
 * - Validaciones
 *
 * Requisito académico: "CRUD operations"
 *
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 4: VIEWMODEL - CONEXIÓN CON UI
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Crear:
 *
 * 📁 viewmodel/
 *    ├── DashboardViewModel.kt
 *    ├── CreateHabitViewModel.kt
 *    ├── HabitsListViewModel.kt
 *    └── AchievementsViewModel.kt
 *
 * Responsabilidades:
 * - Hacer queries a Repository
 * - Actualizar State (LiveData / Flow)
 * - Manejar eventos del usuario
 *
 * Requisito académico: "Manejo de sesión y datos dinámicos"
 *
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 5: UI - CONEXIÓN CON VIEWMODEL
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Modificar:
 *
 * ✅ DashboardScreen.kt
 *    - Ya está hermosa visualmente
 *    - Necesita recibir datos del ViewModel
 *    - Mostrar datos reales en lugar de hardcodeados
 *
 * ✅ CreateHabitScreen.kt
 * ✅ HabitsListScreen.kt
 * ✅ AchievementsScreen.kt
 *
 * Requisito académico: "Navegación correcta entre pantallas"
 *
 * ════════════════════════════════════════════════════════════════════════════════
 * PARTE 6: COMPARTIR DATOS (SharedPreferences)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Necesario para:
 * - Guardar ID del usuario logueado
 * - Mantener sesión entre reinicios
 * - Recordar último personaje
 *
 * Crear:
 *
 * 📁 preferences/
 *    └── PreferencesManager.kt
 *       - saveUserLogin(userId)
 *       - getUserLogin()
 *       - clearUserLogin()
 *
 * Requisito académico: "Manejo de sesión con SharedPreferences"
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * DIAGRAMA DE CAPAS (ARQUITECTURA LIMPIA)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *  ┌─────────────────────────────────────────────────────────────────┐
 *  │                    UI LAYER (Jetpack Compose)                   │
 *  │  DashboardScreen - HabitsListScreen - CreateHabitScreen         │
 *  └────────────────────────────┬────────────────────────────────────┘
 *                               │
 *  ┌────────────────────────────▼────────────────────────────────────┐
 *  │                  VIEWMODEL LAYER                                │
 *  │  DashboardViewModel - CreateHabitViewModel                      │
 *  │  (Observa cambios en datos, actualiza UI)                       │
 *  └────────────────────────────┬────────────────────────────────────┘
 *                               │
 *  ┌────────────────────────────▼────────────────────────────────────┐
 *  │                REPOSITORY LAYER                                 │
 *  │  HabitRepository                                                │
 *  │  (Lógica de negocio: cálculos, validaciones)                    │
 *  └────────────────────────────┬────────────────────────────────────┘
 *                               │
 *  ┌────────────────┬───────────▼──────────┬──────────────────────────┐
 *  │                │                      │                          │
 *  ▼                ▼                      ▼                          ▼
 * ┌──────────┐ ┌──────────────┐    ┌──────────────────┐    ┌──────────────┐
 * │ Room DB  │ │ SharedPrefs  │    │ API (opcional)   │    │ Local Cache  │
 * │ (datos)  │ │ (sesión)     │    │ (future)         │    │ (offline)    │
 * └──────────┘ └──────────────┘    └──────────────────┘    └──────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * DEPENDENCIAS QUE NECESITAS
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Ya debes tener en build.gradle.kts:
 *
 * ✅ Kotlin
 * ✅ Jetpack Compose
 *
 * Necesitas agregar:
 *
 * // Room Database
 * implementation("androidx.room:room-runtime:2.6.1")
 * implementation("androidx.room:room-ktx:2.6.1")
 * kapt("androidx.room:room-compiler:2.6.1")
 *
 * // ViewModel & LiveData
 * implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
 * implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
 *
 * // Coroutines (para queries asincrónicas)
 * implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
 *
 * El proyecto debe compilar sin errores después de agregar estas dependencias
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * CHECKLIST DE REQUISITOS ACADÉMICOS
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * ✅ [DONE] Modelos de datos bien diseñados
 *    - Usuario
 *    - Habito
 *    - ProgressoDiario
 *    - Logro
 *
 * ⏳ [NEXT] Persistencia local con Room
 *    - Entidades
 *    - DAO
 *    - Database
 *
 * ⏳ [NEXT] CRUD Operations
 *    - Crear hábito
 *    - Leer hábitos
 *    - Actualizar progreso
 *    - Eliminar hábito
 *
 * ⏳ [NEXT] Manejo de sesión
 *    - SharedPreferences
 *    - Login/Logout
 *    - Mostrar usuario logueado
 *
 * ⏳ [NEXT] Navegación correcta
 *    - Entre pantallas
 *    - Botón atrás
 *    - Persistencia de estado
 *
 * ⏳ [NEXT] Datos dinámicos
 *    - Desde base de datos
 *    - Actualizaciones en tiempo real
 *    - ViewModel + Compose State
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * PRÓXIMOS PASOS
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Una vez termines de leer esto:
 *
 * 1. Confirma que entiendes los modelos
 * 2. Pregunta si algo no está claro
 * 3. Pasamos al PASO 2: Configurar Room Database
 *
 * No vamos a hacer todo de una vez (como dijiste al inicio)
 * Paso a paso, académicamente, y con explicaciones claras.
 *
 * Let's go! 🚀
 */

