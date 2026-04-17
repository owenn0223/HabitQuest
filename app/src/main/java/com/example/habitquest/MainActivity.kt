package com.example.habitquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.viewmodel.LoginViewModel
import com.example.habitquest.viewmodel.PantallaDestino
import com.example.habitquest.viewmodel.SplashViewModel
import com.example.habitquest.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Permitir que la app dibuje detrás de las barras del sistema
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            HabitQuestTheme {
                // Crear el SesionManager una sola vez
                val sesionManager = SesionManager(this@MainActivity)
                // Crear base de datos y repositorio
                val database = HabitDatabase.getDatabase(this@MainActivity)
                val usuarioRepository = UsuarioRepository(database.usuarioDao())
                AppNavigation(sesionManager = sesionManager, usuarioRepository = usuarioRepository)
            }
        }
    }
}

// Enum para controlar qué pantalla mostrar
enum class Screen {
    SPLASH,        // Pantalla de carga que verifica sesión
    WELCOME,
    CREATE_HERO,
    LOGIN,
    DASHBOARD,
    CREATE_HABIT,
    HABITS_LIST,
    ACHIEVEMENTS,
    PROFILE,
    STATISTICS
}

@Composable
fun AppNavigation(sesionManager: SesionManager, usuarioRepository: UsuarioRepository) {
    val currentScreen = remember { mutableStateOf(Screen.SPLASH) }


    when (currentScreen.value) {
        Screen.SPLASH -> {
            val splashViewModel = SplashViewModel(sesionManager)
            SplashScreen(
                viewModel = splashViewModel,
                onNavegacionDeterminada = { destino ->
                    currentScreen.value = when (destino) {
                        PantallaDestino.HOME -> Screen.DASHBOARD
                        PantallaDestino.LOGIN -> Screen.WELCOME
                    }
                }
            )
        }
        Screen.WELCOME -> WelcomeScreen(
            onCreateCharacterClick = { currentScreen.value = Screen.CREATE_HERO },
            onLoginClick = { currentScreen.value = Screen.LOGIN }
        )
        Screen.CREATE_HERO -> {
            val registroViewModel = RegistroViewModel(usuarioRepository, sesionManager)
            CreateHeroScreen(
                viewModel = registroViewModel,
                onBackClick = { currentScreen.value = Screen.WELCOME },
                onRegistrationSuccess = { currentScreen.value = Screen.DASHBOARD },
                onLoginClick = { currentScreen.value = Screen.LOGIN }
            )
        }
        Screen.LOGIN -> {
            val loginViewModel = LoginViewModel(usuarioRepository, sesionManager)
            LoginScreen(
                viewModel = loginViewModel,
                onBackClick = { currentScreen.value = Screen.WELCOME },
                onLoginSuccess = { currentScreen.value = Screen.DASHBOARD },
                onCreateCharacterClick = { currentScreen.value = Screen.CREATE_HERO }
            )
        }
        Screen.DASHBOARD -> DashboardScreen(
            onCreateHabitClick = { currentScreen.value = Screen.CREATE_HABIT },
            onHabitsListClick = { currentScreen.value = Screen.HABITS_LIST },
            onAchievementsClick = { currentScreen.value = Screen.ACHIEVEMENTS },
            onProfileClick = { currentScreen.value = Screen.PROFILE }
        )
        Screen.CREATE_HABIT -> CreateHabitScreen(
            onBack = { currentScreen.value = Screen.DASHBOARD }
        )
        Screen.HABITS_LIST -> HabitsListScreen(
            onBack = { currentScreen.value = Screen.DASHBOARD },
            onCreateHabit = { currentScreen.value = Screen.CREATE_HABIT },
            onAchievementsClick = { currentScreen.value = Screen.ACHIEVEMENTS },
            onProfileClick = { currentScreen.value = Screen.PROFILE }
        )
        Screen.ACHIEVEMENTS -> AchievementsScreen(
            onBack = { currentScreen.value = Screen.DASHBOARD }
        )
        Screen.PROFILE -> ProfileScreen(
            onBack = { currentScreen.value = Screen.DASHBOARD },
            onStatisticsClick = { currentScreen.value = Screen.STATISTICS },
            onAchievementsClick = { currentScreen.value = Screen.ACHIEVEMENTS },
            onLogout = { currentScreen.value = Screen.WELCOME }
        )
        Screen.STATISTICS -> StatisticsScreen(
            onBack = { currentScreen.value = Screen.PROFILE }
        )
    }
}

@Composable
fun WelcomeScreen(
    onCreateCharacterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a)) // Fondo verde oscuro
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio superior
        Spacer(modifier = Modifier.height(40.dp))

        // LOGO - Cuadrado con borde verde y icono dentro
        // 📍 AQUÍ VA EL ICONO: Reemplaza el Box con un icono real (puedes usar Icons.Default o un recurso drawable)
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF00FF88), // Verde brillante
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = Color(0xFF0d6b4f), // Verde más oscuro dentro del cuadrado
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // 📍 AQUÍ VA EL ICONO DEL CHEQUE/EQUIS
            // Puedes usar: Icon(Icons.Default.Check, ...) o cargar desde drawable
            // Por ahora es un placeholder Text, reemplázalo con un Icon
            Text(
                text = "✔✔",
                fontSize = 60.sp,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // TÍTULO
        Text(
            text = "HabitQuest",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // SUBTÍTULO
        Text(
            text = "Level up your life, one habit at a time",
            fontSize = 16.sp,
            color = Color(0xFF999999), // Gris
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(80.dp))

        // BOTÓN LOG IN (Verde sólido)
        Button(
            onClick = { onLoginClick() },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF88) // Verde brillante
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Log In →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1a3a2a) // Texto oscuro
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN CREATE CHARACTER (Con borde verde)
        Button(
            onClick = { onCreateCharacterClick() },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF00FF88), // Verde brillante
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent // Fondo transparente
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Create Character 👤",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF88) // Texto verde
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        // VERSIÓN (En la parte inferior)
        Text(
            text = "VERSION 1.0.0 · VARIANT 1 OF 10",
            fontSize = 12.sp,
            color = Color(0xFF666666), // Gris oscuro
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    HabitQuestTheme {
        WelcomeScreen(
            onCreateCharacterClick = {},
            onLoginClick = {}
        )
    }
}