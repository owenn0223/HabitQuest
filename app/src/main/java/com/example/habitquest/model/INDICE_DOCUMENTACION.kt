/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📇 ÍNDICE DE DOCUMENTACIÓN - PASO 1
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Guía para navegar toda la documentación creada
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📚 ARCHIVOS PRINCIPALES (LOS MODELOS)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 1️⃣ Usuario.kt
 *    └─ ¿Qué es? El personaje RPG del usuario
 *    └─ Lee esto si quieres entender:
 *       - Estructura del usuario
 *       - Estadísticas RPG
 *       - Qué muestra en el Dashboard
 *    └─ Importante para: Calcular nivel, mostrar datos personales
 *
 * 2️⃣ Habito.kt
 *    └─ ¿Qué es? Cada hábito que crea el usuario
 *    └─ Lee esto si quieres entender:
 *       - Tipos de hábito (DAILY, WEEKLY)
 *       - Dificultad (EASY, MEDIUM, HARD)
 *       - Cómo se relacionan con XP
 *    └─ Importante para: Lista de hábitos, crear hábito
 *
 * 3️⃣ ProgressoDiario.kt ⭐
 *    └─ ¿Qué es? Historial de cada día (MÁS IMPORTANTE)
 *    └─ Lee esto si quieres entender:
 *       - Por qué es crucial
 *       - Cómo permite calcular todo
 *       - Relación con otros modelos
 *    └─ Importante para: Calcular racha, XP, hábitos completados
 *
 * 4️⃣ Logro.kt
 *    └─ ¿Qué es? Achievements desbloqueables
 *    └─ Lee esto si quieres entender:
 *       - Tipos de logros (STREAK, LEVEL, TOTAL_XP)
 *       - Cómo se desbloquean
 *       - Gamificación
 *    └─ Importante para: Pantalla de achievements
 *
 * 5️⃣ DashboardState.kt
 *    └─ ¿Qué es? Estado agregado (patrón MVVM)
 *    └─ Lee esto si quieres entender:
 *       - Por qué necesitamos un estado agregado
 *       - Separación UI-Lógica
 *       - Cómo fluyen los datos
 *    └─ Importante para: Conectar BD con UI
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📖 DOCUMENTACIÓN COMPLETA
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 1️⃣ GUIA_ACADEMICA.kt
 *    └─ TIPO: Explicación teórica completa
 *    └─ NIVEL: Principiante
 *    └─ LEE ESTO CUANDO: Quieras entender la arquitectura completa
 *    └─ CONTENIDO:
 *       ├─ Resumen de cada modelo
 *       ├─ Capas de la aplicación
 *       ├─ Dependencias necesarias
 *       ├─ Checklist de requisitos académicos
 *       └─ Próximos pasos
 *    └─ TIEMPO: 15 minutos
 *
 * 2️⃣ DIAGRAMA_RELACIONES.kt
 *    └─ TIPO: Visualización de tablas y relaciones
 *    └─ NIVEL: Intermedio
 *    └─ LEE ESTO CUANDO: Quieras ver cómo se conectan los datos
 *    └─ CONTENIDO:
 *       ├─ Tablas de BD con ejemplos
 *       ├─ Relaciones (FOREIGN KEYS)
 *       ├─ Flujo de datos en la app
 *       └─ Explicación de relaciones
 *    └─ TIEMPO: 20 minutos
 *
 * 3️⃣ MAPEO_DATOS_UI.kt
 *    └─ TIPO: Mapeo de datos Dashboard
 *    └─ NIVEL: Intermedio
 *    └─ LEE ESTO CUANDO: Quieras saber de dónde viene cada cosa en el Dashboard
 *    └─ CONTENIDO:
 *       ├─ Cada parte del Dashboard
 *       ├─ De dónde obtiene los datos
 *       ├─ Cálculos matemáticos
 *       └─ Ejemplo visual completo
 *    └─ TIEMPO: 25 minutos
 *    └─ MÁS ÚTIL PARA: Conectar UI con datos
 *
 * 4️⃣ EJEMPLOS_DATOS.kt
 *    └─ TIPO: Datos de ejemplo para pruebas
 *    └─ NIVEL: Fácil
 *    └─ LEE ESTO CUANDO: Quieras ver cómo se vería con datos reales
 *    └─ CONTENIDO:
 *       ├─ usuarioEjemplo
 *       ├─ habitosEjemplo (lista completa)
 *       ├─ progresoEjemplo (historial)
 *       ├─ logrosEjemplo
 *       ├─ dashboardStateEjemplo (todo junto)
 *       └─ Cálculos manuales
 *    └─ TIEMPO: 10 minutos
 *    └─ MÁS ÚTIL PARA: Pruebas y desarrollo
 *
 * 5️⃣ QUICK_REFERENCE.kt
 *    └─ TIPO: Referencia rápida
 *    └─ NIVEL: Fácil
 *    └─ LEE ESTO CUANDO: Estés programando y necesites recordar algo rápido
 *    └─ CONTENIDO:
 *       ├─ Tabla 1: USUARIO
 *       ├─ Tabla 2: HABITO
 *       ├─ Tabla 3: PROGRESO_DIARIO
 *       ├─ Tabla 4: LOGRO
 *       ├─ Queries que usaremos
 *       ├─ Cálculos centrales
 *       ├─ Constantes
 *       └─ Helper functions
 *    └─ TIEMPO: 5 minutos (referencia)
 *    └─ MÁS ÚTIL PARA: Durante el desarrollo en Room/Repository
 *
 * 6️⃣ DIAGRAMA_VISUAL_FLUJO.kt
 *    └─ TIPO: Diagramas ASCII de flujos
 *    └─ NIVEL: Intermedio
 *    └─ LEE ESTO CUANDO: Quieras ver los flujos visuales completos
 *    └─ CONTENIDO:
 *       ├─ Flujo al iniciar app
 *       ├─ Flujo de carga del Dashboard
 *       ├─ Cómo se pinta la pantalla
 *       ├─ Cuando completa un hábito
 *       ├─ Cuando crea un hábito
 *       ├─ Estructura de carpetas
 *       └─ Ciclo de vida de requests
 *    └─ TIEMPO: 20 minutos
 *    └─ MÁS ÚTIL PARA: Entender el flujo completo
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📋 RESÚMENES Y GUÍAS
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * 1️⃣ RESUMEN_PASO1.kt (en workspace)
 *    └─ TIPO: Resumen visual
 *    └─ LEE ESTO CUANDO: Quieras un resumen rápido de todo
 *    └─ CONTENIDO: Lo más importante del Paso 1
 *    └─ TIEMPO: 10 minutos
 *
 * 2️⃣ PASO_1_COMPLETADO.md (mostrado)
 *    └─ TIPO: Resumen ejecutivo
 *    └─ LEE ESTO CUANDO: Termines de leer todo y quieras cerrar
 *    └─ CONTENIDO: Lo más importante
 *    └─ TIEMPO: 5 minutos
 *
 * 3️⃣ RESUMEN_VISUAL_PASO_1.md (mostrado)
 *    └─ TIPO: Resumen visual con tablas
 *    └─ LEE ESTO CUANDO: Quieras ver todo de forma visual
 *    └─ CONTENIDO: Estructura, cálculos, requisitos
 *    └─ TIEMPO: 15 minutos
 *
 * 4️⃣ RESUMEN_EJECUTIVO_PASO_1.md (mostrado)
 *    └─ TIPO: Resumen para ejecutivos/profesores
 *    └─ LEE ESTO CUANDO: Vayas a presentar
 *    └─ CONTENIDO: Lo profesional del proyecto
 *    └─ TIEMPO: 10 minutos
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🎯 GUÍA DE LECTURA POR OBJETIVO
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * OBJETIVO: "Entender la estructura de datos básica"
 *
 * Lee en este orden:
 * 1. Este archivo (ÍNDICE)
 * 2. Usuario.kt
 * 3. Habito.kt
 * 4. ProgressoDiario.kt
 * 5. RESUMEN_PASO1.kt
 *
 * Tiempo: 20 minutos
 */

/**
 * OBJETIVO: "Entender cómo fluyen los datos en la app"
 *
 * Lee en este orden:
 * 1. DIAGRAMA_VISUAL_FLUJO.kt
 * 2. DIAGRAMA_RELACIONES.kt
 * 3. MAPEO_DATOS_UI.kt
 * 4. QUICK_REFERENCE.kt
 *
 * Tiempo: 45 minutos
 */

/**
 * OBJETIVO: "Estar listo para implementar Room (PASO 2)"
 *
 * Lee en este orden:
 * 1. QUICK_REFERENCE.kt (memoriza tablas)
 * 2. EJEMPLOS_DATOS.kt (entiende el volumen de datos)
 * 3. GUIA_ACADEMICA.kt (entiende el contexto)
 * 4. DIAGRAMA_RELACIONES.kt (visualiza las queries)
 *
 * Tiempo: 60 minutos
 */

/**
 * OBJETIVO: "Explicar todo a alguien más (profesor, equipo)"
 *
 * Usa:
 * 1. RESUMEN_EJECUTIVO_PASO_1.md
 * 2. DIAGRAMA_VISUAL_FLUJO.kt
 * 3. MAPEO_DATOS_UI.kt
 * 4. EJEMPLOS_DATOS.kt (mostrar números reales)
 *
 * Tiempo: Según duración de presentación
 */

/**
 * OBJETIVO: "Programar Room (PASO 2)"
 *
 * Abre en paralelo:
 * 1. QUICK_REFERENCE.kt (consulta rápida)
 * 2. EJEMPLOS_DATOS.kt (referencia de volumen)
 * 3. DIAGRAMA_RELACIONES.kt (queries que necesitas)
 *
 * Tiempo: Durante programación
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🔍 BÚSQUEDA RÁPIDA
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * ¿DÓNDE ENCONTRAR...?
 *
 * "Cómo se calcula la racha"
 * → QUICK_REFERENCE.kt (CÁLCULO 2: RACHA ACTUAL)
 * → DIAGRAMA_RELACIONES.kt (últimas secciones)
 *
 * "Cómo se calcula el nivel"
 * → QUICK_REFERENCE.kt (CÁLCULO 4: NIVEL Y PROGRESO)
 * → MAPEO_DATOS_UI.kt (CÁLCULO MATEMÁTICO)
 *
 * "Ejemplo de datos reales"
 * → EJEMPLOS_DATOS.kt (todo el archivo)
 * → MAPEO_DATOS_UI.kt (EJEMPLO REAL: DASHBOARD CON DATOS)
 *
 * "Estructura de carpetas completa"
 * → DIAGRAMA_VISUAL_FLUJO.kt (sección 6)
 * → QUICK_REFERENCE.kt (TODO LIST)
 *
 * "Relaciones entre tablas"
 * → DIAGRAMA_RELACIONES.kt (todo el archivo)
 * → QUICK_REFERENCE.kt (RELACIONES)
 *
 * "Queries que usaremos"
 * → QUICK_REFERENCE.kt (sección de cada tabla)
 * → DIAGRAMA_RELACIONES.kt (CÁLCULOS)
 *
 * "Dashboard explicado en detalle"
 * → MAPEO_DATOS_UI.kt (partes 1-5)
 * → DIAGRAMA_VISUAL_FLUJO.kt (sección 3)
 *
 * "Flujo cuando completa un hábito"
 * → DIAGRAMA_VISUAL_FLUJO.kt (sección 4)
 *
 * "Flujo cuando crea un hábito"
 * → DIAGRAMA_VISUAL_FLUJO.kt (sección 5)
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 📊 MATRIZ DE CONTENIDO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Tema                | Archivo Principal        | Ref. Secundaria
 * ─────────────────────────────────────────────────────────────────────
 * Conceptos           | GUIA_ACADEMICA           | -
 * Datos reales        | EJEMPLOS_DATOS           | MAPEO_DATOS_UI
 * Flujo visual        | DIAGRAMA_VISUAL_FLUJO    | -
 * Queries             | QUICK_REFERENCE          | DIAGRAMA_RELACIONES
 * Tablas BD           | DIAGRAMA_RELACIONES      | QUICK_REFERENCE
 * UI-Datos            | MAPEO_DATOS_UI           | DIAGRAMA_VISUAL_FLUJO
 * Cálculos            | QUICK_REFERENCE          | MAPEO_DATOS_UI
 * Resumen rápido      | RESUMEN_PASO1            | -
 * Para presentar      | RESUMEN_EJECUTIVO        | DIAGRAMA_VISUAL_FLUJO
 * Referencia durante  | QUICK_REFERENCE          | EJEMPLOS_DATOS
 * desarrollo          |                          |
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * ✅ CHECKLIST: "He leído todo y entiendo"
 * ════════════════════════════════════════════════════════════════════════════════
 */

/**
 * MARCAR CONFORME LEES:
 *
 * □ Usuario.kt
 * □ Habito.kt
 * □ ProgressoDiario.kt
 * □ Logro.kt
 * □ DashboardState.kt
 * □ GUIA_ACADEMICA.kt
 * □ DIAGRAMA_RELACIONES.kt
 * □ MAPEO_DATOS_UI.kt
 * □ EJEMPLOS_DATOS.kt
 * □ QUICK_REFERENCE.kt
 * □ DIAGRAMA_VISUAL_FLUJO.kt
 * □ RESUMEN_VISUAL_PASO_1.md
 * □ RESUMEN_EJECUTIVO_PASO_1.md
 *
 * ¿TODO MARCADO? Felicidades, ¡entiendes Paso 1! 🎉
 */

/**
 * ════════════════════════════════════════════════════════════════════════════════
 * 🚀 LISTO PARA PASO 2?
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Antes de continuar, verifica que:
 *
 * ✅ Entiendes por qué ProgressoDiario es crucial
 * ✅ Puedes explicar cómo se calcula la racha
 * ✅ Puedes explicar cómo se calcula el nivel
 * ✅ Entiende el flujo: BD → Repository → ViewModel → UI
 * ✅ Conoces las 4 tablas principales
 * ✅ Entiende el patrón MVVM con DashboardState
 *
 * Si TODO está ✅, estás ready para PASO 2: ROOM DATABASE
 */

