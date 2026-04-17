# 🚀 Quick Start Guide: Crear Hábitos

## ⚡ En 5 Minutos

### 1. **Abre HabitsListScreen**
```
Pantalla con lista de hábitos
```

### 2. **Presiona el botón + (Floating Action Button)**
```
Navega a CreateHabitScreen
```

### 3. **Rellena el formulario**

#### Campo 1: Nombre del Hábito
```
Ingresa: "Morning Meditation" (mínimo 3 caracteres)
```

#### Campo 2: Selecciona Frecuencia
```
☐ DAILY      ← Predeterminado
☐ WEEKLY
☐ MONTHLY
```

#### Campo 3: Selecciona Dificultad
```
☐ EASY (😊 - 10 XP)    ← Predeterminado
☐ MEDIUM (⚡ - 20 XP)
☐ HARD (💀 - 40 XP)
```

#### Campo 4: Selecciona Atributo RPG
```
☐ Strength (🏋️)     ← Predeterminado
☐ Intelligence (🧠)
☐ Agility (🏃)
☐ Charisma (🗣️)
```

### 4. **Presiona "BEGIN QUEST ⚔️"**
```
El botón está deshabilitado hasta que llenes el nombre
```

### 5. **¡Hecho! 🎉**
```
✓ Mensaje de éxito
✓ Vuelves a la lista
✓ Ves tu nuevo hábito
```

---

## 🎯 Ubicaciones Clave

| Componente | Ubicación |
|-----------|-----------|
| ViewModel | `viewmodel/CreateHabitViewModel.kt` |
| Pantalla | `CreateHabitScreen.kt` |
| Modelo | `model/Habit.kt` |
| BD | `database/HabitDatabase.kt` |
| DAO | `database/HabitDao.kt` |

---

## 🔄 Flujo Visual

```
┌─────────────────────────────────┐
│   HABITSLILST SCREEN            │
│                                 │
│  [←] HabitQuest    ⚡ LVL 14 [⚙] │
│                                 │
│  [All] Daily Weekly Monthly     │
│                                 │
│  🎯 Hábito 1                    │
│  🎯 Hábito 2                    │
│  🎯 Hábito 3                    │
│                        [+]      │  ← Click aquí
│                                 │
└─────────────────────────────────┘
                  ↓
┌─────────────────────────────────┐
│   CREATE HABIT SCREEN           │
│                                 │
│  [←] New Quest                  │
│                                 │
│  QUEST DETAILS                  │
│  [____________] Nombre          │
│                                 │
│  FREQUENCY                      │
│  [DAILY] [WEEKLY] [MONTHLY]     │
│                                 │
│  DIFFICULTY LEVEL               │
│  [😊 EASY] [⚡ MED] [💀 HARD]   │
│   10 XP    20 XP    40 XP       │
│                                 │
│  RPG ATTRIBUTE FOCUS            │
│  [🏋️ Strength]  [🧠 Intel]      │
│  [🏃 Agility]    [🗣️ Charisma]  │
│                                 │
│  [⚔️ BEGIN QUEST]               │
│                                 │
└─────────────────────────────────┘
                  ↓
         (Se guarda en BD)
                  ↓
┌─────────────────────────────────┐
│   HABITSLILST SCREEN (actualizado)      │
│                                 │
│  [←] HabitQuest    ⚡ LVL 14 [⚙] │
│                                 │
│  [All] Daily Weekly Monthly     │
│                                 │
│  🎯 Hábito 1                    │
│  🎯 Hábito 2                    │
│  🎯 Hábito 3                    │
│  🎯 Morning Meditation ⭐ NUEVO │
│                        [+]      │
│                                 │
└─────────────────────────────────┘
```

---

## 📊 Estados del Botón

### ❌ Deshabilitado (gris oscuro)
```
Condiciones:
- Campo de nombre está vacío
- Sistema está creando (cargando)

Aspecto:
[CREATING...] ⌛
```

### ✅ Habilitado (verde brillante)
```
Condiciones:
- Campo de nombre tiene contenido
- No está cargando

Aspecto:
[⚔️ BEGIN QUEST]
```

---

## 💬 Mensajes del Sistema

### ✅ Éxito (Verde)
```
┌─────────────────────────────┐
│ ✓ ¡Hábito creado exitosamente! 🎉 ✕ │
└─────────────────────────────┘
```

### ❌ Error (Rojo)
```
┌─────────────────────────────┐
│ El nombre del hábito no puede... ✕ │
└─────────────────────────────┘
```

---

## 🔑 Validaciones que Debes Conocer

| Validación | Mín | Máx | Error |
|-----------|-----|-----|-------|
| Nombre vacío | - | - | "El nombre no puede estar vacío" |
| Nombre muy corto | 3 | - | "Mínimo 3 caracteres" |
| Nombre muy largo | - | 50 | "Máximo 50 caracteres" |

---

## 📱 Ejemplos de Hábitos a Crear

```
1. Morning Meditation
   📊 Daily, Easy, Strength
   ⚡ 10 XP

2. Heavy Lifting Session
   📊 Weekly, Hard, Strength
   ⚡ 40 XP

3. Read 20 Pages
   📊 Daily, Medium, Intelligence
   ⚡ 20 XP

4. 10k Steps Walk
   📊 Daily, Easy, Agility
   ⚡ 10 XP

5. Practice Public Speaking
   📊 Weekly, Hard, Charisma
   ⚡ 40 XP
```

---

## 🎓 ¿Qué pasa después?

### Cuando creas un hábito:

```
1. ✓ Se guarda en ROOM DATABASE
   └─ Acceso principal de la app
   └─ Sincronizable si agrega backend

2. ✓ Se guarda en SHARED PREFERENCES (JSON)
   └─ Respaldo local
   └─ Recuperable manualmente
   └─ Portable (puede exportarse)

3. ✓ Se muestra en HABITSLIST SCREEN
   └─ Con frecuencia (badge)
   └─ Con dificultad (color)
   └─ Con XP (cantidad)

4. ✓ Puedes MARCAR COMO COMPLETADO
   └─ Presionar botón + → ✓
   └─ Suma XP al usuario
   └─ Resetea diariamente

5. ✓ Puedes ELIMINAR
   └─ Presionar botón 🗑️
   └─ Confirmación antes de borrar
```

---

## 🛠️ Troubleshooting Rápido

| Problema | Solución |
|----------|----------|
| ❌ Botón deshabilitado | Escribe algo en el nombre |
| ❌ No guarda | Comprueba logs, revisa validación |
| ❌ No aparece en lista | Vuelve a HabitsListScreen |
| ❌ Mensaje no desaparece | Haz click en ✕ para cerrarlo |
| ❌ Validación extraña | Revisa que mínimo sean 3 caracteres |

---

## ⭐ Características Especiales

### 1. XP Automático
```
Seleccionas dificultad → XP se calcula solo
EASY ➜ 10 XP
MED ➜ 20 XP
HARD ➜ 40 XP
```

### 2. Fechas Automáticas
```
Fecha de creación: Se agrega automáticamente
Formato: YYYY-MM-DD (ej: 2024-04-16)
```

### 3. Persistencia Dual
```
Room DB + SharedPreferences JSON
Redundancia para mayor seguridad
```

### 4. Selecciones Visuales
```
Lo que seleccionas se ilumina en VERDE
Fácil de ver qué está seleccionado
```

---

## 📈 Estadísticas Automáticas

El sistema automáticamente:
- ✅ Cuenta hábitos totales
- ✅ Cuenta hábitos completados
- ✅ Suma XP total (si la app lo implementa)
- ✅ Filtra por frecuencia
- ✅ Mostrará tendencias (próximamente)

---

## 🎯 Próximos Pasos Sugeridos

```
1️⃣ Crear tu primer hábito (Morning Meditation)
2️⃣ Marcar como completado
3️⃣ Crear otro hábito (Heavy Lifting)
4️⃣ Filtrar por "Weekly"
5️⃣ Verificar que aparecen en la lista
6️⃣ Eliminar uno para probar
7️⃣ ¡Disfrutar del sistema! 🚀
```

---

## 📞 ¿Necesitas Ayuda?

**Revisa estos archivos en orden**:
1. `RESUMEN_IMPLEMENTACION.md` - Visión general
2. `GUIA_CREAR_HABITO.md` - Documentación técnica
3. `EJEMPLOS_CREAR_HABITO.md` - Código de ejemplo

---

## ✨ ¡Estás Listo! 

Ahora puedes crear, guardar y gestionar hábitos de forma completa con:
- ✅ Interfaz intuitiva
- ✅ Validación robusta
- ✅ Persistencia confiable
- ✅ Mensajes claros
- ✅ Integración perfecta

¡A crear hábitos! 🎉


