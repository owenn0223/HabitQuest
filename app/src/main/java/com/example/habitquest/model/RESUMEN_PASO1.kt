/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📊 RESUMEN - PASO 1 COMPLETADO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Ya hemos creado los MODELOS DE DATOS que tu Dashboard necesita.
 *
 * Aquí está exactamente qué creamos y por qué:
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 1️⃣ USUARIO.KT - El personaje RPG del usuario
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * data class Usuario(
 *     val id: Int = 1,                    // Solo 1 usuario por app
 *     val nombre: String,                 // "Ironclad Guardian"
 *     val clase: String,                  // "WARRIOR"
 *     val nivelActual: Int = 1,           // Nivel 1-∞
 *     val xpActual: Int = 0,              // XP dentro del nivel (0-100)
 *     val xpTotal: Int = 0,               // XP acumulado desde inicio
 *     val rachaActual: Int = 0,           // 15 días seguidos
 *     val ultimaFecha: String = "",       // Para calcular racha
 *     val disciplina: Int = 10,           // Estadística RPG
 *     val fuerza: Int = 10,               // Estadística RPG
 *     val inteligencia: Int = 10,         // Estadística RPG
 *     val consistencia: Int = 10          // Estadística RPG
 * )
 *
 * EN EL DASHBOARD SE MUESTRA:
 * ├─ nombre → "Ironclad Guardian" (arriba)
 * ├─ clase → "WARRIOR CLASS" (color verde)
 * ├─ nivelActual → "LVL 12" (badge)
 * ├─ xpActual + xpTotal → "450 / 1000" (barra de progreso)
 * └─ rachaActual → "15 Days" (en card)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 2️⃣ HABITO.KT - Cada hábito que crea el usuario
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * data class Habito(
 *     val id: Int = 0,                    // ID único (autoincrement en BD)
 *     val nombre: String,                 // "Morning Exercise"
 *     val descripcion: String,            // "Perform 20 minutes of cardio"
 *     val dificultad: String,             // "EASY" | "MEDIUM" | "HARD"
 *     val frecuencia: String,             // "DAILY" | "WEEKLY"
 *     val xpRecompensa: Int,              // 10, 20 o 40 XP
 *     val tipo: String,                   // "STUDY", "EXERCISE", "HEALTH", "GENERAL"
 *     val fechaCreacion: String,          // "2024-01-15"
 *     val estado: String = "ACTIVE",      // "ACTIVE" | "ARCHIVED"
 *     val horaSugerida: String = "",      // "08:30" (para ordenar)
 *     val emoji: String = "⚔️"            // Para mostrar bonito en UI
 * )
 *
 * EN EL DASHBOARD SE MUESTRA:
 * ├─ En "Current Quest":
 * │  ├─ emoji + nombre → "⚔️ Morning Vitality"
 * │  ├─ descripcion → "Perform 20 minutes of cardio"
 * │  ├─ xpRecompensa → "+50 XP"
 * │  └─ horaSugerida → "08:30 AM"
 * │
 * └─ En lista de hábitos:
 *    ├─ nombre → "Morning Exercise"
 *    └─ Si completado hoy → mostrar check ✓
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 3️⃣ PROGRESO_DIARIO.KT - Historial de cada día
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * data class ProgressoDiario(
 *     val id: Int = 0,                    // ID único
 *     val habitoId: Int,                  // ¿Cuál hábito? (relación FK)
 *     val fecha: String,                  // "2024-01-15" (qué día)
 *     val completado: Boolean = false,    // ¿Se completó? (TRUE/FALSE)
 *     val xpGanado: Int = 0,              // ¿Cuántos XP ganó?
 *     val horaCompletado: String = ""     // "08:30" (a qué hora)
 * )
 *
 * ESTO ES LO MÁS IMPORTANTE porque aquí se guardan TODOS los datos históricos.
 *
 * CÁLCULOS QUE PERMITE:
 *
 * ✓ "Habits Today" (4/8):
 *   SELECT COUNT(*) FROM progreso_diario
 *   WHERE fecha = TODAY AND completado = TRUE
 *
 * ✓ "Current Streak" (15 days):
 *   Verificar día a día hacia atrás si todos los hábitos = completado
 *   Detener cuando encuentra un día incompleto
 *
 * ✓ "Total Lifetime XP" (12,450):
 *   SELECT SUM(xpGanado) FROM progreso_diario
 *   WHERE completado = TRUE
 *
 * ✓ "Progress Bar" (450/1000):
 *   Usar xpTotal para calcular nivel actual
 *   Y XP faltante para próximo nivel
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 4️⃣ LOGRO.KT - Achievements desbloqueables
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * data class Logro(
 *     val id: Int = 0,                    // ID único
 *     val nombre: String,                 // "7 Day Champion"
 *     val descripcion: String,            // "Completa 7 días seguidos"
 *     val icono: String = "🏆",          // "🏆", "⭐", "👑", etc
 *     val condicion: String,              // "STREAK", "LEVEL", "TOTAL_XP", "HABITS_COMPLETED"
 *     val valor: Int,                     // 7 (para 7 días) o 100 (para 100 XP)
 *     val desbloqueado: Boolean = false,  // ¿Ya lo logró?
 *     val fechaDesbloqueo: String = ""    // "2024-01-16" (cuándo lo desbloqueó)
 * )
 *
 * EN LA PANTALLA DE ACHIEVEMENTS SE MUESTRA:
 * ├─ Logros desbloqueados: con icono visible y fecha
 * └─ Logros bloqueados: con icono gris y "Complete 7 days to unlock"
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 5️⃣ DASHBOARD_STATE.KT - Estado agregado (MVVM)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * data class DashboardState(
 *     val usuario: Usuario,                       // Los datos del usuario
 *     val habitos: List<Habito> = emptyList(),   // Todos sus hábitos
 *     val habitosCompletadosHoy: Int = 0,        // 4 de 8
 *     val totalHabitos: Int = 0,                 // 8 total
 *     val rachaActual: Int = 0,                  // 15 días
 *     val xpTotalAcumulado: Int = 0,             // 12,450
 *     val xpActualNivel: Int = 0,                // 450
 *     val xpNecesarioProximoNivel: Int = 0,      // 1000
 *     val nivelActual: Int = 1,                  // 12
 *     val proximoHabito: Habito? = null,         // El siguiente a completar
 *     val porcentajeProgreso: Float = 0f         // 0.45 (45%)
 * )
 *
 * VENTAJA:
 * La UI SOLO recibe esto. No calcula nada.
 * El ViewModel/Repository hace todos los cálculos.
 *
 * Esto es lo que se llama SEPARACIÓN DE RESPONSABILIDADES
 * (Requisito académico importante)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * RESUMEN VISUAL - QUÉ INFORMACIÓN FLUYE POR LA APP
 * ════════════════════════════════════════════════════════════════════════════════
 *
 *
 *                          ┌─────────────────────────┐
 *                          │      USUARIO ABRE       │
 *                          │    HABITQUEST APP       │
 *                          └────────────┬────────────┘
 *                                       │
 *                                       ▼
 *                          ┌─────────────────────────┐
 *                          │  ¿Sesión iniciada?      │
 *                          └────────┬────────────────┘
 *                                   │
 *                ┌──────────────────┼──────────────────┐
 *                │                  │                  │
 *               NO                  SI              (primera vez)
 *                │                  │
 *                ▼                  ▼
 *          WELCOME SCREEN       DASHBOARD
 *             (login)               │
 *                │                  │
 *          [Crear o Login]          ▼
 *                │          Repository.getDashboardState()
 *                │                  │
 *                │          ┌───────┴────────┬─────────────┐
 *                │          │                │             │
 *                │          ▼                ▼             ▼
 *                │      DB: Usuario      DB: Habito    DB: Progreso
 *                │          │                │             │
 *                │          └────────────────┴─────────────┘
 *                │                      │
 *                │                      ▼
 *                │        (Calcular racha, XP, nivel)
 *                │                      │
 *                │                      ▼
 *                │            DashboardState retornado
 *                │                      │
 *                │                      ▼
 *                │            DashboardScreen recibe datos
 *                │                      │
 *                │                      ▼
 *                │            Pinta UI con datos reales
 *                │
 *                └──────────────────────┘
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * ✅ CHECKLIST - PASO 1 COMPLETADO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * ✅ Modelo Usuario creado
 * ✅ Modelo Habito creado
 * ✅ Modelo ProgressoDiario creado
 * ✅ Modelo Logro creado
 * ✅ Modelo DashboardState creado
 * ✅ Diagramas y explicaciones documentadas
 * ✅ Guía académica clara
 *
 * ¿ENTIENDES BIEN?
 * Si no, pregunta antes de seguir al siguiente paso.
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🚀 PRÓXIMO PASO: CONFIGURAR ROOM DATABASE
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * En el PASO 2 vamos a:
 *
 * 1. Agregar dependencias de Room al build.gradle.kts
 * 2. Convertir modelos en @Entity (tablas de BD)
 * 3. Crear DAO (Data Access Objects) para queries
 * 4. Crear la Database
 *
 * Esto es el requisito académico: "Persistencia local con Room"
 *
 * Confirma que entiendes los modelos y pasamos al siguiente paso 👍
 */

