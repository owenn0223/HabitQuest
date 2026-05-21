package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitquest.viewmodel.PantallaDestino
import com.example.habitquest.viewmodel.SplashViewModel

/**
 * Pantalla de splash (carga)
 * Verifica si hay sesión activa y navega al destino correspondiente
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavegacionDeterminada: (destino: PantallaDestino) -> Unit
) {
    val destino = viewModel.destinoNavegacion.collectAsState().value

    // Cuando se determine el destino, ejecutar la navegación
    LaunchedEffect(destino) {
        destino?.let { onNavegacionDeterminada(it) }
    }

    // Mostrar el logo mientras se determina el destino
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a)), // Fondo verde oscuro
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // LOGO - Cuadrado con borde verde
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF00FF88), // Verde brillante
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = Color(0xFF0d6b4f), // Verde más oscuro dentro
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✔✔",
                fontSize = 60.sp,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold
            )
        }

        // TÍTULO
        Text(
            text = "HabitQuest",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

