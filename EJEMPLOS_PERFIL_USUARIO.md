# 🎯 Ejemplos de Integración: Sistema de Perfil de Usuario

## 📱 Ejemplo 1: Integración Básica con Navegación

### En tu archivo de navegación (MainActivity.kt o NavGraph.kt):

```kotlin
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(
                onCreateHabitClick = { navController.navigate("createHabit") },
                onHabitsListClick = { navController.navigate("habitsList") },
                onAchievementsClick = { navController.navigate("achievements") },
                onProfileClick = { navController.navigate("profile") } // ✅ NUEVO
            )
        }

        composable("profile") { // ✅ NUEVO
            ProfileScreen(
                onBack = { navController.navigateUp() },
                onStatisticsClick = { navController.navigate("statistics") },
                onAchievementsClick = { navController.navigate("achievements") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) // Limpiar back stack
                    }
                }
            )
        }

        composable("statistics") { // ✅ NUEVO
            StatisticsScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable("createHabit") {
            CreateHabitScreen(
                onBack = { navController.navigateUp() },
                onBeginQuest = { navController.navigateUp() }
            )
        }

        composable("habitsList") {
            HabitsListScreen(
                onBack = { navController.navigateUp() },
                onCreateHabit = { navController.navigate("createHabit") },
                onAchievementsClick = { navController.navigate("achievements") }
            )
        }

        composable("achievements") {
            AchievementsScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("dashboard") }
            )
        }

        // ... otras rutas
    }
}
```

---

## 🎨 Ejemplo 2: Usar ProfileViewModel Directamente

### Si quieres acceder al ViewModel desde otro composable:

```kotlin
@Composable
fun CustomProfileComponent() {
    val viewModel: ProfileViewModel = viewModel()

    // Acceso a estados
    val currentUser by viewModel.currentUser.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Mostrar información básica
    currentUser?.let { user ->
        Column {
            Text("Nombre: ${user.nombre}")
            Text("Clase: ${viewModel.getClassDisplayName(user.clase)}")
            Text("Nivel: ${user.nivelActual}")

            if (isEditMode) {
                Button(onClick = { viewModel.saveProfileChanges() }) {
                    Text("Guardar Cambios")
                }
                Button(onClick = { viewModel.cancelEdit() }) {
                    Text("Cancelar")
                }
            } else {
                Button(onClick = { viewModel.enableEditMode() }) {
                    Text("Editar Perfil")
                }
            }

            errorMessage?.let {
                Text("Error: $it", color = Color.Red)
            }
        }
    }
}
```

---

## 💾 Ejemplo 3: Recuperar Datos del Usuario

### Para mostrar información del usuario en cualquier pantalla:

```kotlin
@Composable
fun UserInfoHeader() {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()

    currentUser?.let { user ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0d6b4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    profileViewModel.getClassIcon(user.clase),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    user.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "LVL ${user.nivelActual} ${profileViewModel.getClassDisplayName(user.clase)}",
                    color = Color(0xFF00FF88),
                    fontSize = 12.sp
                )
            }
        }
    }
}
```

---

## 🔄 Ejemplo 4: Flujo Completo de Edición

```kotlin
@Composable
fun ProfileEditor() {
    val viewModel: ProfileViewModel = viewModel()

    // Estados
    val editName by viewModel.editName.collectAsState()
    val editEmail by viewModel.editEmail.collectAsState()
    val editClass by viewModel.editClass.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Campo nombre
        OutlinedTextField(
            value = editName,
            onValueChange = { viewModel.updateEditName(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo email
        OutlinedTextField(
            value = editEmail,
            onValueChange = { viewModel.updateEditEmail(it) },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de clase
        Text("Clase:", fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth()) {
            viewModel.getAvailableClasses().forEach { userClass ->
                Button(
                    onClick = { viewModel.updateEditClass(userClass) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (editClass == userClass) Color(0xFF00FF88) else Color.Gray
                    )
                ) {
                    Text(viewModel.getClassDisplayName(userClass))
                }
                if (userClass != "ADVENTURER") Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensajes
        errorMessage?.let {
            Text("❌ $it", color = Color.Red)
        }
        successMessage?.let {
            Text("✅ $it", color = Color.Green)
        }

        // Botones
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.cancelEdit() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.saveProfileChanges() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}
```

---

## 📊 Ejemplo 5: Mostrar Estadísticas Personalizadas

```kotlin
@Composable
fun CustomStatsDisplay() {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()

    currentUser?.let { user ->
        Column(modifier = Modifier.padding(16.dp)) {
            // Nivel con progreso
            Text("Nivel ${user.nivelActual}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { profileViewModel.getLevelProgressPercentage() },
                modifier = Modifier.fillMaxWidth()
            )
            Text("${user.xpActual}/100 XP")

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas RPG
            Text("Estadísticas RPG:", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem("🎯", "Discipline", user.disciplina)
                StatItem("💪", "Strength", user.fuerza)
                StatItem("🧠", "Intelligence", user.inteligencia)
                StatItem("⚡", "Consistency", user.consistencia)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // XP Total
            Text("XP Total: ${user.xpTotal}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatItem(icon: String, label: String, value: Int) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 24.sp)
        Text(label, fontSize = 12.sp)
        Text("$value", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
```

---

## 🔐 Ejemplo 6: Verificación de Sesión

### Para proteger rutas que requieren login:

```kotlin
@Composable
fun ProtectedRoute(
    content: @Composable () -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()

    if (currentUser != null) {
        // Usuario logueado, mostrar contenido
        content()
    } else {
        // Usuario no logueado, redirigir a login
        // navController.navigate("login")
        Text("Debes iniciar sesión")
    }
}

// Uso:
@Composable
fun ProtectedProfileScreen() {
    ProtectedRoute {
        ProfileScreen(
            onBack = { /* ... */ },
            onStatisticsClick = { /* ... */ },
            onAchievementsClick = { /* ... */ },
            onLogout = { /* ... */ }
        )
    }
}
```

---

## 🎬 Ejemplo 7: Integración con Dashboard

### Para mostrar información del usuario en el dashboard:

```kotlin
@Composable
fun DashboardWithUserInfo() {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()

    Column {
        // Header con info del usuario
        currentUser?.let { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0d6b4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profileViewModel.getClassIcon(user.clase), fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        user.nombre,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "LVL ${user.nivelActual} ${profileViewModel.getClassDisplayName(user.clase)}",
                        color = Color(0xFF00FF88),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón de perfil
                IconButton(onClick = { /* navigate to profile */ }) {
                    Icon(Icons.Default.Person, tint = Color.White)
                }
            }
        }

        // Resto del dashboard...
        DashboardContent()
    }
}
```

---

## 🚀 Ejemplo 8: Extender con Nuevas Estadísticas

```kotlin
@Composable
fun ExtendedStatistics() {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()

    currentUser?.let { user ->
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text("Estadísticas Avanzadas", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Estadísticas básicas
            item {
                StatCard("🏆", "Nivel Actual", user.nivelActual.toString())
                StatCard("⭐", "XP Total", user.xpTotal.toString())
                StatCard("🔥", "Racha Actual", "${user.rachaActual} días")
            }

            // Estadísticas RPG
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Atributos RPG", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard("🎯", "Discipline", user.disciplina.toString())
                    StatCard("💪", "Strength", user.fuerza.toString())
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard("🧠", "Intelligence", user.inteligencia.toString())
                    StatCard("⚡", "Consistency", user.consistencia.toString())
                }
            }

            // Cálculos adicionales
            item {
                Spacer(modifier = Modifier.height(16.dp))
                val totalStats = user.disciplina + user.fuerza + user.inteligencia + user.consistencia
                val avgStats = totalStats / 4.0
                StatCard("📊", "Promedio de Atributos", "%.1f".format(avgStats))
            }
        }
    }
}

@Composable
private fun StatCard(icon: String, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF203c2e))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, modifier = Modifier.weight(1f), color = Color.White)
            Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
        }
    }
}
```

---

## 🎯 Checklist de Integración

- [x] Agregar rutas de navegación para "profile" y "statistics"
- [x] Conectar DashboardScreen.onProfileClick
- [x] Conectar ProfileScreen.onStatisticsClick
- [x] Conectar ProfileScreen.onAchievementsClick (a ruta existente)
- [x] Conectar ProfileScreen.onLogout
- [x] Verificar que SesionManager esté funcionando
- [x] Probar edición de perfil
- [x] Probar navegación entre pantallas
- [x] Verificar que los datos se guarden correctamente

---

## 🚨 Troubleshooting

### "No se muestra la información del usuario"
```
1. Verificar que el usuario esté logueado
2. Revisar SesionManager.obtenerUsuarioId()
3. Verificar UsuarioDao.getUsuarioById()
4. Comprobar logs de error
```

### "Los cambios no se guardan"
```
1. Verificar validaciones en saveProfileChanges()
2. Revisar UsuarioDao.updateUsuario()
3. Verificar SesionManager.guardarSesion()
4. Comprobar permisos de BD
```

### "Error al cerrar sesión"
```
1. Verificar SesionManager.cerrarSesion()
2. Revisar navegación a login
3. Verificar popUpTo(0) para limpiar stack
```

### "No navega correctamente"
```
1. Verificar nombres de rutas en NavGraph
2. Revisar onClick handlers
3. Verificar navController.navigate()
```

---

## 💡 Tips Útiles

1. **Debug**: Agrega `println()` en métodos del ViewModel para ver el flujo
2. **Testing**: Crea un usuario de prueba y verifica todas las funciones
3. **UI**: Personaliza colores en `Color(0xFF...)` según tu tema
4. **Performance**: Los datos se cargan una vez al inicializar el ViewModel
5. **Offline**: Todo funciona sin conexión (datos locales)

---

## 🎉 ¡Listo para Integrar!

Con estos ejemplos puedes integrar perfectamente el sistema de perfil en tu aplicación. Todo está diseñado para ser:

- ✅ **Fácil de integrar**
- ✅ **Altamente reutilizable**
- ✅ **Escalable**
- ✅ **Mantenible**

**¡Éxito con tu perfil de usuario! 🚀**
