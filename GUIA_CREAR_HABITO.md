# 📋 Guía de Implementación: Sistema de Creación de Hábitos

## 🎯 Descripción General

Se ha implementado un sistema completo de creación de hábitos siguiendo los patrones arquitectónicos de la aplicación HabitQuest. El sistema incluye:

- **ViewModel reactivo** con StateFlow para gestionar el estado
- **Validación de datos** en tiempo real
- **Persistencia dual**: Room Database + SharedPreferences
- **Serialización JSON** para datos portables
- **Interfaz interactiva** con feedback visual

---

## 📁 Archivos Creados/Modificados

### 1. **CreateHabitViewModel.kt** (NUEVO)
**Ubicación**: `app/src/main/java/com/example/habitquest/viewmodel/`

**Responsabilidades**:
- Gestionar estado del formulario (nombre, frecuencia, dificultad, atributo)
- Validar datos antes de guardar
- Crear nuevos hábitos en Room Database
- Guardar hábitos en SharedPreferences (respaldo)
- Emitir mensajes de éxito/error

**Estados (StateFlow)**:
```kotlin
_habitName: String                    // Nombre del hábito
_selectedFrequency: String           // "DAILY", "WEEKLY", "MONTHLY"
_selectedDifficulty: String          // "EASY", "MED", "HARD"
_selectedAttribute: String           // Atributo RPG seleccionado
_isLoading: Boolean                  // Indicador de carga
_errorMessage: String?               // Mensaje de error
_successMessage: String?             // Mensaje de éxito
```

**Métodos Principales**:
```kotlin
fun setHabitName(name: String)                      // Actualizar nombre
fun setFrequency(frequency: String)                 // Cambiar frecuencia
fun setDifficulty(difficulty: String)               // Cambiar dificultad
fun setAttribute(attribute: String)                 // Cambiar atributo
fun createHabit(onSuccess: () -> Unit)              // Crear y guardar hábito
fun getHabitsFromSharedPreferences(): List<Habit>   // Recuperar de SP
fun clearErrorMessage()                             // Limpiar error
fun clearSuccessMessage()                           // Limpiar éxito
```

### 2. **CreateHabitScreen.kt** (MODIFICADO)
**Ubicación**: `app/src/main/java/com/example/habitquest/`

**Cambios**:
- ✅ Integración con CreateHabitViewModel
- ✅ Captura de datos en todos los campos
- ✅ Validación visual (campos resaltados)
- ✅ Mensajes de error/éxito interactivos
- ✅ Indicador de carga durante creación
- ✅ Botón deshabilitado hasta llenar nombre

**Flujo de UI**:
```
Usuario rellena formulario
        ↓
setHabitName/setFrequency/setDifficulty/setAttribute
        ↓
Estados se actualizan en StateFlow
        ↓
UI se redibuja (collectAsState)
        ↓
Usuario presiona "BEGIN QUEST"
        ↓
createHabit() ejecuta validación y guardado
        ↓
Mostrar mensaje de éxito → Resetear formulario → Navegar
        O
Mostrar mensaje de error → Usuario puede reintentar
```

---

## 🔐 Cálculo de XP por Dificultad

| Dificultad | XP | Icono |
|-----------|----|----|
| EASY      | 10 | 😊 |
| MED       | 20 | ⚡ |
| HARD      | 40 | 💀 |

---

## 💾 Persistencia de Datos

### Room Database
```
Tabla: habit
├── id (PrimaryKey, AutoIncrement)
├── nombre (String)
├── frecuencia (String: "DAILY", "WEEKLY", "MONTHLY")
├── dificultad (String: "EASY", "MED", "HARD")
├── xp (Int)
├── completado (Boolean)
├── fechaCreacion (String: "yyyy-MM-dd")
└── ultimaVezCompletado (String: "yyyy-MM-dd")
```

### SharedPreferences
```json
CLAVE: "lista_habitos_json"
VALOR: [
  {
    "id": 1,
    "nombre": "Morning Meditation",
    "frecuencia": "DAILY",
    "dificultad": "EASY",
    "xp": 10,
    "completado": false,
    "fechaCreacion": "2024-04-16",
    "ultimaVezCompletado": ""
  },
  ...
]
```

---

## ✅ Validaciones Implementadas

1. **Nombre no vacío**: Verifica que el usuario ingrese un nombre
2. **Longitud mínima**: Mínimo 3 caracteres
3. **Longitud máxima**: Máximo 50 caracteres
4. **Trimming**: Elimina espacios en blanco antes/después

```kotlin
private fun validateHabitName(): Boolean {
    val name = _habitName.value.trim()
    return name.length >= 3 && name.length <= 50
}
```

---

## 🎨 Interfaz de Usuario

### Estados Visuales

**Campo de Nombre**:
- ✅ Vinculado a StateFlow
- ✅ Se habilita/deshabilita según carga
- ✅ Color verde cuando está activo

**Frecuencia** (Selección única):
- ✅ DAILY (predeterminado)
- ✅ WEEKLY
- ✅ MONTHLY
- ✅ Resaltado en verde cuando está seleccionado

**Dificultad** (Selección única):
- ✅ EASY (predeterminado) - 10 XP
- ✅ MED - 20 XP
- ✅ HARD - 40 XP
- ✅ Muestra XP debajo de cada opción

**Atributo RPG** (Selección única):
- ✅ Strength (predeterminado) 🏋️
- ✅ Intelligence 🧠
- ✅ Agility 🏃
- ✅ Charisma 🗣️

**Botón BEGIN QUEST**:
- ✅ Deshabilitado si nombre está vacío
- ✅ Deshabilitado durante carga
- ✅ Muestra spinner cuando está creando
- ✅ Colores deshabilitados oscurecidos

**Mensajes de Estado**:
- ✅ Error: Fondo rojo, texto blanco, botón ✕
- ✅ Éxito: Fondo verde, texto oscuro, botón ✓
- ✅ Auto-desaparecen o usuario puede cerrarlos

---

## 🔄 Ciclo de Vida Completo

```
1. Usuario abre CreateHabitScreen
   ↓
2. Se crea instancia de CreateHabitViewModel
   ↓
3. Estados se inicializan:
   - habitName = ""
   - selectedFrequency = "DAILY"
   - selectedDifficulty = "EASY"
   - selectedAttribute = "Strength"
   ↓
4. Usuario interactúa (rellena formulario)
   ↓
5. Cada cambio actualiza StateFlow (set*)
   ↓
6. UI se redibuja automáticamente (collectAsState)
   ↓
7. Usuario presiona "BEGIN QUEST"
   ↓
8. createHabit() valida datos
   ↓
9a. Si válido:
    - Calcular XP según dificultad
    - Crear objeto Habit
    - insertHabit() en Room
    - saveHabitToSharedPreferences() en SP
    - Mostrar mensaje éxito
    - Resetear formulario
    - Ejecutar callback onSuccess (navegación)
    ↓
9b. Si inválido:
    - Mostrar mensaje error específico
    - Usuario puede reintentar
```

---

## 📚 Patrones Usados

### 1. **MVVM (Model-View-ViewModel)**
- **Model**: Clase `Habit` (entity Room)
- **ViewModel**: `CreateHabitViewModel`
- **View**: `CreateHabitScreen` composable

### 2. **StateFlow para Reactividad**
```kotlin
// Privado para modificar
private val _habitName = MutableStateFlow("")

// Público para observar
val habitName: StateFlow<String> = _habitName

// En UI: collectAsState() convierte Flow en State
val habitName by viewModel.habitName.collectAsState()
```

### 3. **Serialización JSON**
```kotlin
// Convertir Habit → JSON
val habitJson = JSONObject().apply {
    put("nombre", habit.nombre)
    put("frecuencia", habit.frecuencia)
    // ...
}

// Almacenar en SharedPreferences
sharedPreferences.edit().putString("lista_habitos_json", array.toString()).apply()
```

### 4. **Corrutinas Scoped**
```kotlin
viewModelScope.launch {
    // Se ejecuta en background
    // Se cancela automáticamente cuando ViewModel se destruye
    habitDao.insertHabit(newHabit)
}
```

---

## 🚀 Cómo Usar

### 1. **Desde otro composable**:
```kotlin
// En HabitsListScreen o Dashboard
onCreateHabit = {
    navController.navigate("createHabit")
}
```

### 2. **Navegar después de crear**:
```kotlin
// En CreateHabitScreen
onBeginQuest = {
    // El callback se ejecuta cuando el hábito se crea exitosamente
    navController.navigateUp()  // Volver a pantalla anterior
}
```

### 3. **Recuperar hábitos de SharedPreferences** (si necesitas):
```kotlin
val habitsFromSP = viewModel.getHabitsFromSharedPreferences()
```

---

## 🔗 Integración con Existentes

### Con HabitsListViewModel
```kotlin
// Los hábitos creados automáticamente aparecen en la lista
// porque HabitsListViewModel observa habitDao.getAllHabits()
// que incluye los nuevos hábitos insertados
```

### Con SesionManager
```kotlin
// Si necesitas asociar hábito a usuario:
val userId = sesionManager.obtenerUsuarioId()
// Podrías extender Habit con campo usuarioId
```

---

## 📝 Notas Importantes

1. **Thread Safety**: ✅ Room maneja threading automáticamente con `suspend`
2. **Lifecycle**: ✅ viewModelScope se cancela cuando screen se destruye
3. **Memory Leaks**: ✅ StateFlow no causa leaks (Compose maneja lifecycle)
4. **Offline**: ✅ Room + SharedPreferences permiten funcionamiento offline
5. **Backup**: ✅ SharedPreferences proporciona respaldo JSON

---

## 🛠️ Próximas Mejoras Sugeridas

1. **Edición de hábitos**: Reutilizar ViewModel para editar
2. **Foto/Icono**: Campo para seleccionar icono personalizado
3. **Descripción**: Campo de descripción larga
4. **Recordatorios**: Integrar con NotificationManager
5. **Categorías**: Agrupar hábitos por categoría
6. **Estadísticas**: Mostrar progreso vs meta

---

## ✨ Características Implementadas

- [x] Captura de nombre del hábito
- [x] Selección de frecuencia (Daily/Weekly/Monthly)
- [x] Selección de dificultad (Easy/Med/Hard)
- [x] Cálculo automático de XP
- [x] Selección de atributo RPG
- [x] Validación de datos
- [x] Guardado en Room Database
- [x] Guardado en SharedPreferences (JSON)
- [x] Mensajes de error interactivos
- [x] Mensajes de éxito interactivos
- [x] Indicador de carga visual
- [x] Botón deshabilitado mientras carga
- [x] Reseteo automático de formulario
- [x] Fechas automáticas (yyyy-MM-dd)
- [x] Serialización Habit ↔ JSON

---

## 📖 Referencias Arquitectónicas

**Siguiente en el flujo**:
1. HabitsListScreen puede mostrar hábito recién creado
2. Agregar edición de hábitos (reutilizar ViewModel)
3. Agregar eliminación confirmada
4. Sincronización de datos (si aplica)

**Patrón de estado**:
- Todos los campos del formulario son `StateFlow`
- Cambios automáticos disparan redibujado
- Un único ViewModel por pantalla


