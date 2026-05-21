package com.example.habitquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.database.UsuarioRepository
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.navigation.AppNavigation
import com.example.habitquest.ui.theme.HabitQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            HabitQuestTheme {
                val sesionManager = SesionManager(this@MainActivity)
                val database = HabitDatabase.getDatabase(this@MainActivity)
                val usuarioRepository = UsuarioRepository(database.usuarioDao())
                AppNavigation(
                    sesionManager = sesionManager,
                    usuarioRepository = usuarioRepository
                )
            }
        }
    }
}
