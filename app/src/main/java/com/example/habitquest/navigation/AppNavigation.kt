package com.example.habitquest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.ui.screens.achievements.AchievementsScreen
import com.example.habitquest.ui.screens.createhabit.CreateHabitScreen
import com.example.habitquest.ui.screens.createhero.CreateHeroScreen
import com.example.habitquest.ui.screens.createhero.RegistroViewModel
import com.example.habitquest.ui.screens.dashboard.DashboardScreen
import com.example.habitquest.ui.screens.habits.HabitsListScreen
import com.example.habitquest.ui.screens.login.LoginScreen
import com.example.habitquest.ui.screens.login.LoginViewModel
import com.example.habitquest.ui.screens.profile.ProfileScreen
import com.example.habitquest.ui.screens.splash.PantallaDestino
import com.example.habitquest.ui.screens.splash.SplashScreen
import com.example.habitquest.ui.screens.splash.SplashViewModel
import com.example.habitquest.ui.screens.statistics.StatisticsScreen
import com.example.habitquest.ui.screens.welcome.WelcomeScreen

enum class Screen {
    SPLASH,
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
                onNavegacionDeterminada = { destino: PantallaDestino ->
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
