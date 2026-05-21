# 🎯 Ejemplos de Uso: CreateHabitScreen & CreateHabitViewModel

## 📱 Ejemplo 1: Integración Básica con Navegación

### En tu archivo de Navegación (ej: NavGraph.kt o MainActivity.kt):

```kotlin
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "habitsList"
    ) {
        composable("habitsList") {
            HabitsListScreen(
                onBack = { navController.navigateUp() },
                onCreateHabit = { navController.navigate("createHabit") },
                onAchievementsClick = { navController.navigate("achievements") }
            )
        }

        composable("createHabit") {
            CreateHabitScreen(
                onBack = { navController.navigateUp() },
                onBeginQuest = {
                    // Volver a la lista después de crear
                    navController.navigate("habitsList") {
                        popUpTo("habitsList") { inclusive = false }
                    }
                }
            )
        }

        // Otras pantallas...
    }
}
```

---

## 🎨 Ejemplo 2: Usar CreateHabitViewModel Directamente

### Si quieres acceder al ViewModel desde otra pantalla:

```kotlin
@Composable
fun MyScreen() {
    val viewModel: CreateHabitViewModel = viewModel()
    
    // Acceso a los estados
    val habitName by viewModel.habitName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    
    // Acciones
    Button(onClick = {
        viewModel.setHabitName("Mi Nuevo Hábito")
        viewModel.setFrequency("WEEKLY")
        viewModel.setDifficulty("HARD")
        
        viewModel.createHabit {
            println("¡Hábito creado exitosamente!")
        }
    }) {
        Text("Crear Hábito")
    }
}
```

---

## 💾 Ejemplo 3: Recuperar Hábitos Guardados en SharedPreferences

```kotlin
@Composable
fun LoadHabitsScreen() {
    val viewModel: CreateHabitViewModel = viewModel()
    var habitsFromSP by remember { mutableStateOf<List<Habit>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        // Recuperar hábitos de SharedPreferences
        habitsFromSP = viewModel.getHabitsFromSharedPreferences()
    }
    
    LazyColumn {
        items(habitsFromSP) { habit ->
            HabitCard(habit = habit)
        }
    }
}
```

---

## 🔄 Ejemplo 4: Flujo Completo

```kotlin
@Composable
fun HabitCreationFlow() {
    val viewModel: CreateHabitViewModel = viewModel()
    
    // Estados
    val habitName by viewModel.habitName.collectAsState()
    val selectedFrequency by viewModel.selectedFrequency.collectAsState()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val selectedAttribute by viewModel.selectedAttribute.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    
    Column {
        // 1. Nombre
        TextField(
            value = habitName,
            onValueChange = { viewModel.setHabitName(it) },
            label = { Text("Nombre del Hábito") },
            enabled = !isLoading
        )
        
        // 2. Frecuencia
        Row {
            listOf("DAILY", "WEEKLY", "MONTHLY").forEach { freq ->
                Button(
                    onClick = { viewModel.setFrequency(freq) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFrequency == freq) Color.Green else Color.Gray
                    )
                ) {
                    Text(freq)
                }
            }
        }
        
        // 3. Dificultad
        Row {
            listOf("EASY", "MED", "HARD").forEach { diff ->
                Button(
                    onClick = { viewModel.setDifficulty(diff) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDifficulty == diff) Color.Blue else Color.Gray
                    )
                ) {
                    Text(diff)
                }
            }
        }
        
        // 4. Mensajes
        errorMessage?.let {
            Text("Error: $it", color = Color.Red)
        }
        successMessage?.let {
            Text("Éxito: $it", color = Color.Green)
        }
        
        // 5. Botón crear
        Button(
            onClick = {
                viewModel.createHabit {
                    println("Hábito creado: $habitName")
                }
            },
            enabled = !isLoading && habitName.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Text("Creando...")
            } else {
                Text("Crear Hábito")
            }
        }
    }
}
```

---

## 🗂️ Ejemplo 5: Guardar Hábito Personalizado

```kotlin
@Composable
fun SaveCustomHabit() {
    val viewModel: CreateHabitViewModel = viewModel()
    
    // Datos predefinidos
    val customData = mapOf(
        "nombre" to "Hacer Ejercicio",
        "frecuencia" to "DAILY",
        "dificultad" to "HARD",
        "atributo" to "Strength"
    )
    
    LaunchedEffect(Unit) {
        // Llenar formulario
        viewModel.setHabitName(customData["nombre"]!!)
        viewModel.setFrequency(customData["frecuencia"]!!)
        viewModel.setDifficulty(customData["dificultad"]!!)
        viewModel.setAttribute(customData["atributo"]!!)
        
        // Crear inmediatamente
        viewModel.createHabit {
            println("Hábito creado automáticamente")
        }
    }
    
    Text("Creando hábito personalizado...")
}
```

---

## 🎓 Ejemplo 6: Validación Avanzada

```kotlin
@Composable
fun AdvancedValidationScreen() {
    val viewModel: CreateHabitViewModel = viewModel()
    val habitName by viewModel.habitName.collectAsState()
    
    Column {
        OutlinedTextField(
            value = habitName,
            onValueChange = { viewModel.setHabitName(it) },
            label = { Text("Nombre") },
            isError = habitName.isNotBlank() && !viewModel.isValidHabitName(),
            supportingText = {
                if (habitName.isNotBlank() && !viewModel.isValidHabitName()) {
                    Text(
                        "Mínimo 3 caracteres, máximo 50",
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                }
            }
        )
        
        Button(
            onClick = { viewModel.createHabit() },
            enabled = viewModel.isValidHabitName()
        ) {
            Text("Crear")
        }
    }
}
```

---

## 📊 Ejemplo 7: Mostrar XP Calculado

```kotlin
@Composable
fun ShowXPCalculation() {
    val viewModel: CreateHabitViewModel = viewModel()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val currentXp = viewModel.getCurrentXp()
    
    Box(
        modifier = Modifier
            .background(Color(0xFF0d6b4f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡ ", fontSize = 18.sp)
            Text(
                "XP a ganar: $currentXp",
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

---

## 🔐 Ejemplo 8: Verificar si Hábito Existe

```kotlin
@Composable
fun CheckDuplicateHabits() {
    val viewModel: CreateHabitViewModel = viewModel()
    val habitName by viewModel.habitName.collectAsState()
    val habitsInSP = viewModel.getHabitsFromSharedPreferences()
    
    val habitExists = habitsInSP.any { 
        it.nombre.equals(habitName.trim(), ignoreCase = true) 
    }
    
    if (habitExists) {
        Text(
            "⚠️ Ya existe un hábito con este nombre",
            color = Color.Yellow
        )
    }
}
```

---

## 🎬 Ejemplo 9: Flujo con Estados

```kotlin
sealed class HabitCreationState {
    object Idle : HabitCreationState()
    object Loading : HabitCreationState()
    data class Success(val habitId: Int) : HabitCreationState()
    data class Error(val message: String) : HabitCreationState()
}

@Composable
fun HabitCreationWithStates() {
    val viewModel: CreateHabitViewModel = viewModel()
    var state: HabitCreationState by remember { mutableStateOf(HabitCreationState.Idle) }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    
    // Actualizar estado según ViewModel
    LaunchedEffect(isLoading, errorMessage, successMessage) {
        state = when {
            isLoading -> HabitCreationState.Loading
            errorMessage != null -> HabitCreationState.Error(errorMessage!!)
            successMessage != null -> HabitCreationState.Success(1) // ID dummy
            else -> HabitCreationState.Idle
        }
    }
    
    when (state) {
        is HabitCreationState.Idle -> Text("Listo para crear")
        is HabitCreationState.Loading -> CircularProgressIndicator()
        is HabitCreationState.Success -> Text("¡Éxito!")
        is HabitCreationState.Error -> Text("Error: ${(state as HabitCreationState.Error).message}")
    }
}
```

---

## 📱 Ejemplo 10: Integración con HabitsListScreen

```kotlin
// En HabitsListScreen.kt, el botón + ya navega a CreateHabitScreen
// Cuando se crea exitosamente, se ejecuta onBeginQuest() que navega de vuelta
// El nuevo hábito automáticamente aparece en la lista porque:
// 1. CreateHabitViewModel inserta en Room
// 2. HabitsListViewModel observa habitDao.getAllHabits()
// 3. El Flow emite el nuevo hábito
// 4. La UI se redibuja con el nuevo hábito

// Ahora los hábitos creados aparecen instantáneamente en la lista
```

---

## 🎯 Checklist de Integración

- [ ] CreateHabitViewModel está en `viewmodel/`
- [ ] CreateHabitScreen está actualizado y vinculado al ViewModel
- [ ] Navigation incluye la ruta "createHabit"
- [ ] HabitsListScreen tiene botón para ir a CreateHabitScreen
- [ ] Se pueden crear hábitos exitosamente
- [ ] Hábitos aparecen en la lista después de crear
- [ ] SharedPreferences almacena los hábitos
- [ ] Validaciones funcionan correctamente
- [ ] Mensajes de error/éxito se muestran
- [ ] Indicador de carga funciona

---

## 🚨 Troubleshooting

### "No se guarda el hábito"
```
1. Verifica que habitDao.insertHabit() es llamado
2. Comprueba que Room está configurada en HabitDatabase
3. Revisa los logs de Android Studio
```

### "El mensaje de éxito no se muestra"
```
1. Verifica que successMessage StateFlow se actualiza
2. Comprueba que clearSuccessMessage() es llamado al cerrar
```

### "El formulario no se resetea"
```
1. Verifica que resetForm() es llamado en createHabit()
2. Comprueba que todos los campos se resetean
```

### "No aparece en SharedPreferences"
```
1. Verifica que saveHabitToSharedPreferences() es llamado
2. Comprueba el nombre de la clave "lista_habitos_json"
3. Usa adb shell para inspeccionar SP
```

---

## 💡 Tips Útiles

1. **Debug**: Agrega `println()` en createHabit() para ver el flujo
2. **Testing**: Crea varios hábitos para verificar que aparecen en la lista
3. **UI**: Prueba con nombre muy largo para ver validación
4. **Performance**: Los hábitos se guardan async, OK para no bloquear UI
5. **Offline**: Funciona sin internet gracias a Room


