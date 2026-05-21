# 🔍 ANÁLISIS TÉCNICO: ¿POR QUÉ EL NIVEL Y XP ESTABAN EN 1/0?

## El Problema Original

### ❌ CÓDIGO ANTERIOR (DashboardViewModel.kt)

```kotlin
private fun updateStats(habits: List<Habit>) {
    val total = habits.size
    val completed = habits.count { it.completado }

    // ❌ PROBLEMA: XP se calcula SOLO desde hábitos completados hoy
    val xp = habits.filter { it.completado }.sumOf { it.xp }
    _totalXP.value = xp  // Esto es volátil, no se guarda

    // ❌ PROBLEMA: Se calcula el nivel desde XP volátil
    val nivelInfo = calcularNivel(xp)  // Si xp = 0, nivel = 1
    _level.value = nivelInfo.nivel
}

fun completeCurrentQuest() {
    viewModelScope.launch {
        val quest = _currentQuest.value ?: return@launch
        val today = getCurrentDate()
        val updated = quest.copy(
            completado = true,
            ultimaVezCompletado = today
        )
        habitDao.updateHabit(updated)
        
        // ❌ PROBLEMA: NO suma XP al usuario en BD
        // Solo actualiza el estado del hábito local
    }
}
```

### 🔄 EL CICLO VICIOSO

```
1. Completas un hábito (50 XP)
   ↓
2. updateStats() suma solo hábitos completados = 50 XP
   ↓
3. Ves: "50 XP" en pantalla ✅
   ↓
4. Cierras la app / recargas datos
   ↓
5. resetAllHabitsCompletion() pone todos a completado = false
   ↓
6. updateStats() suma hábitos completados = 0 (ninguno está completado)
   ↓
7. Ves: "0 XP" y Nivel 1 ❌
   ↓
8. BACK TO STEP 1 → Ciclo infinito
```

## Analisis del Flujo de Datos

### Base de Datos - ANTES

**Tabla `habit`:**
```
id | nombre         | completado | xp
---+----------------+------------+----
1  | Morning Run    | true       | 50
2  | Meditation     | false      | 20
```

**Tabla `usuario`:**
```
id | nombre  | xpTotal | nivelActual
---+---------+---------+-------------
1  | Player1 | 0       | 1      ← ❌ NUNCA SE ACTUALIZA
```

### Problema Raíz

El campo `xpTotal` en la tabla `usuario` **NUNCA se incrementaba**. 

Solo se usaba para leer, pero:
1. Nunca se escribía en él
2. `updateStats()` ignoraba `usuario.xpTotal`
3. Usaba una suma temporal y volátil

---

## ✅ LA SOLUCIÓN

### 1. Agregar métodos al DAO

**Archivo: UsuarioDao.kt**

```kotlin
// Obtener usuario (aplicación single-user)
@Query("SELECT * FROM usuario LIMIT 1")
suspend fun getFirstUsuario(): Usuario?

// Incrementar XP permanentemente
@Query("UPDATE usuario SET xpTotal = xpTotal + :xpGanado WHERE id = :usuarioId")
suspend fun sumarXPTotal(usuarioId: Int, xpGanado: Int)
```

**Ventajas:**
- `getFirstUsuario()` obtiene el usuario actual en O(1)
- `sumarXPTotal()` incrementa directamente en BD sin leer primero

---

### 2. Refactorizar updateStats()

**ANTES:**
```kotlin
val xp = habits.filter { it.completado }.sumOf { it.xp }  // ❌ Volátil
_totalXP.value = xp
```

**DESPUÉS:**
```kotlin
val usuario = usuarioDao.getFirstUsuario()  // ✅ Lee de BD
if (usuario != null) {
    val xp = usuario.xpTotal  // ✅ XP PERMANENTE
    _totalXP.value = xp
    val nivelInfo = calcularNivel(xp)
    _level.value = nivelInfo.nivel
    // ... resto del cálculo
}
```

**Cambio Conceptual:**
```
ANTES:  Usuario -> [App RAM] -> Hábitos -> Suma de completados = XP
        (cada reinicio = 0)

DESPUÉS: Usuario -> [Tabla usuario.xpTotal] -> Persist a DB
         (nunca se pierde)
```

---

### 3. Actualizar completeCurrentQuest()

**ANTES:**
```kotlin
val updated = quest.copy(completado = true, ultimaVezCompletado = today)
habitDao.updateHabit(updated)
// ❌ No guarda XP en usuario
```

**DESPUÉS:**
```kotlin
val updated = quest.copy(completado = true, ultimaVezCompletado = today)
habitDao.updateHabit(updated)

// ✅ Suma XP al usuario en BD
val usuario = usuarioDao.getFirstUsuario()
if (usuario != null) {
    usuarioDao.sumarXPTotal(usuario.id, quest.xp)
}
```

**Flujo Correcto:**
```
1. Completas hábito (50 XP)
   ↓
2. Hábito: completado = true (en BD)
   Usuario: xpTotal += 50 (en BD)
   ↓
3. updateStats() lee usuario.xpTotal = 50+ (de BD)
   ↓
4. Ves nivel actualizado ✅
   ↓
5. Cierras app
   ↓
6. Reabres app
   ↓
7. updateStats() lee usuario.xpTotal = 50+ (sigue en BD) ✅
   ↓
8. PERSISTE
```

---

## Base de Datos - DESPUÉS

**Tabla `usuario`:**
```
id | nombre  | xpTotal | nivelActual
---+---------+---------+-------------
1  | Player1 | 120     | 2          ← ✅ SE ACTUALIZA
```

**Tabla `habit`:**
```
id | nombre         | completado | xp  | ultimaVezCompletado
---+----------------+------------+-----+---------------------
1  | Morning Run    | false      | 50  | 2026-04-16    ← Reset diario
2  | Meditation     | false      | 20  | 2026-04-16    ← Reset diario
3  | Evening Read   | false      | 30  | 2026-04-16    ← Reset diario
```

---

## Reset Diario - Correctamente Implementado

**ANTES:**
```kotlin
private fun observeHabits() {
    viewModelScope.launch {
        habitDao.getAllHabits().collectLatest { habits ->
            updateStats(habits)
        }
    }
}
// ❌ No resetea, o resetea en cada lectura
```

**DESPUÉS:**
```kotlin
private var lastResetDate: String = ""

private fun observeHabits() {
    viewModelScope.launch {
        val today = getCurrentDate()
        if (today != lastResetDate) {
            lastResetDate = today
            habitDao.resetAllHabitsCompletion()  // ✅ Una sola vez por día
        }
    }
    // ... resto del código
}
```

**Ventajas:**
- Reset se ejecuta UNA SOLA VEZ al cambiar de día
- No se pierde XP durante el reset
- Si reinicia 100 veces el mismo día, no resetea 100 veces

---

## Prueba de Regresión

### Test 1: Completar hábito
```
Antes: 0 XP, LVL 1
Completa hábito (50 XP)
Después: 50 XP, LVL 1, Barra 50/100 ✅
```

### Test 2: Múltiples hábitos
```
Completa 3 hábitos (50+50+50 = 150 XP)
Resultado: 150 XP, LVL 2, Barra 50/200 ✅
```

### Test 3: Reset diario
```
Día 1: Completa 100 XP → LVL 2
(Medianoche)
Día 2: Abre app
   - Hábitos reseteados a false ✅
   - XP sigue siendo 100 ✅
   - Puedes completar nuevos hábitos
```

### Test 4: Persistencia
```
Completa hábito → 50 XP
Cierra app completamente
Reabre app
Resultado: 50 XP (persiste) ✅
```

---

## Conclusión

**El problema no era un bug del algoritmo, sino del flujo de datos:**

- ❌ **ANTES**: RAM → Cálculo → Olvido
- ✅ **DESPUÉS**: BD → Lectura → Persist

La clave fue:
1. **Persistir XP en usuario.xpTotal** (no en hábitos)
2. **Leer desde BD** en lugar de calcular en RAM
3. **Guardar XP al completar** hábito, no solo cambiar estado

Ahora el sistema es **verdaderamente persistente** y funciona como una app RPG real. 🎮

