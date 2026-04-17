# ✅ PRUEBA DEL SISTEMA DE XP Y NIVEL - CORREGIDO

## Lo que cambió

Se corrigió el problema donde el nivel y XP se quedaban en 1/0. Ahora:

1. **XP se guarda permanentemente** en la base de datos del usuario
2. **Los hábitos se resetean diariamente** sin perder el XP acumulado
3. **Nivel se calcula correctamente** desde el XP total histórico

---

## 📋 PRUEBA PASO A PASO

### Paso 1: Instala la app actualizada
```bash
cd C:\Users\balle\OneDrive\Desktop\Proyecto-apps\HabitQuest
./gradlew.bat installDebug
```

### Paso 2: Abre la app y crea una cuenta

- Ve a **Login/Registro**
- Crea un usuario (ej: `test@test.com` / `password123`)
- Elige una clase (ej: Warrior)

### Paso 3: Crea un hábito

- Click en **+** (crear hábito)
- Nombre: "Test Habit"
- Dificultad: **HARD** (40 XP)
- Frecuencia: DAILY

### Paso 4: Completa el hábito

En la pantalla de **Dashboard**:
- Deberías ver un **Current Quest** con tu hábito
- El hábito debe mostrar: **+40 XP**
- Click en el **botón de completar** (ej: ▶️)

---

## ✅ RESULTADOS ESPERADOS

### INMEDIATAMENTE después de completar:
```
🎯 Nivel: 1
📊 XP: 40 / 100
🔋 Barra: 40% llena
🪙 Total XP: 40
```

### ESPERA UN MOMENTO (actualizar DB)
- Si aún muestra 0, cierra y reabre la app

---

## 📈 PRUEBA MÁS AVANZADA

### Sube a Nivel 2:

1. **Completa varios hábitos** para alcanzar 100+ XP
   - Hábito 1: +40 XP = Total: 40
   - Hábito 2: +40 XP = Total: 80
   - Hábito 3: +40 XP = Total: 120 ✅ SUBE A NIVEL 2

2. **Resultado al llegar a 120 XP:**
```
🎯 Nivel: 2
📊 XP: 20 / 200  (20 XP en el nivel 2, necesita 200 para nivel 3)
🔋 Barra: 10% llena (20/200)
🪙 Total XP: 120
```

---

## 🔄 PRUEBA DEL RESET DIARIO

1. **Completa todos los hábitos del día**
   - Dashboard mostrará: "Hábitos Today: 3/3"
   - Ver: "¡Todos los hábitos completados! 🎉"

2. **Espera a que pasen 24 horas (o cambia la hora del teléfono a mañana)**

3. **Reabre la app**
   - ✅ Los hábitos se resetean a **"Hábitos Today: 0/3"**
   - ✅ El XP **persiste** (sigue siendo 120+)
   - ✅ El nivel **no cambia** inesperadamente

---

## 🐛 SI AÚN HAY PROBLEMAS

### Problema: "Aún muestra LVL 1 / 0 XP"

**Solución 1: Fuerza la actualización**
```kotlin
// En DashboardScreen, presiona el botón de completar nuevamente
// Espera 2-3 segundos
// La BD debería actualizarse
```

**Solución 2: Verifica la base de datos**
```bash
adb shell
cd /data/data/com.example.habitquest/databases/
sqlite3 habitquest.db
SELECT id, nombre, xpTotal FROM usuario;
```

Debería mostrar `xpTotal` mayor a 0 después de completar un hábito.

**Solución 3: Reinicia la app**
- Cierra completamente (no minimices)
- Abre de nuevo
- Los valores deberían actualizarse desde BD

---

## 📝 CAMBIOS TÉCNICOS REALIZADOS

### Archivo: `UsuarioDao.kt`
✅ Agregado:
- `getFirstUsuario()` → Obtiene el usuario actual
- `sumarXPTotal()` → Suma XP al XP total

### Archivo: `DashboardViewModel.kt`
✅ Modificado:
- Ahora obtiene `xpTotal` desde Usuario (no desde hábitos)
- `completeCurrentQuest()` ahora suma XP al usuario en BD
- Se ejecuta `resetAllHabitsCompletion()` al cambiar de día
- Nivel se calcula desde `usuario.xpTotal` (persistente)

### Resultado:
- XP Total = Permanente ✅
- Hábitos = Reseteables diariamente ✅
- Nivel = Basado en XP permanente ✅

---

## 🎯 CONCLUSIÓN

El sistema ahora funciona como una verdadera aplicación RPG:
- 💰 Ganas XP y se guarda para siempre
- ⚔️ Completas quests (hábitos) diariamente
- 📈 Tu nivel sube permanentemente
- 🔄 Cada día tienes nuevos quests que completar

¡A jugar! 🎮

