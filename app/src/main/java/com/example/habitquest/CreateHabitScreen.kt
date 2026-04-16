package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.viewmodel.CreateHabitViewModel

@Composable
fun CreateHabitScreen(
    onBack: () -> Unit = {},
    viewModel: CreateHabitViewModel = viewModel()
) {
    // Observar estados del ViewModel
    val nombre by viewModel.nombre.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val frecuencia by viewModel.frecuencia.collectAsState()
    val dificultad by viewModel.dificultad.collectAsState()
    val tipo by viewModel.tipo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val habitCreado by viewModel.habitCreado.collectAsState()

    // Cuando el hábito se guarda exitosamente, navegar atrás
    LaunchedEffect(habitCreado) {
        if (habitCreado) onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()) // Para que no se corte en pantallas pequeñas
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // ---- Header ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "New Quest",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ---- Nombre del hábito ----
        Text(
            text = "QUEST DETAILS",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                viewModel.nombre.value = it
                viewModel.limpiarError()
            },
            placeholder = { Text("e.g., Morning Meditation", color = Color(0xFF55ffb0)) },
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (errorMessage != null) Color.Red else Color(0xFF00FF88),
                focusedBorderColor = if (errorMessage != null) Color.Red else Color(0xFF00FF88),
                cursorColor = Color(0xFF00FF88),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        // Mensaje de error
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---- Descripción (opcional) ----
        OutlinedTextField(
            value = descripcion,
            onValueChange = { viewModel.descripcion.value = it },
            placeholder = { Text("Short description (optional)", color = Color(0xFF55ffb0)) },
            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF00FF88),
                focusedBorderColor = Color(0xFF00FF88),
                cursorColor = Color(0xFF00FF88),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // ---- Frecuencia ----
        Text(
            text = "FREQUENCY",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("DAILY", "WEEKLY", "MONTHLY").forEach { opcion ->
                val seleccionado = frecuencia == opcion
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (seleccionado) Color(0xFF00FF88) else Color(0xFF203c2e))
                        .clickable { viewModel.frecuencia.value = opcion }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcion,
                        color = if (seleccionado) Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                if (opcion != "MONTHLY") Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ---- Dificultad ----
        Text(
            text = "DIFFICULTY LEVEL",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf(
                Triple("EASY", "😊", "10 XP"),
                Triple("MED", "⚡", "20 XP"),
                Triple("HARD", "💀", "40 XP")
            ).forEach { (nivel, icono, xpLabel) ->
                val seleccionado = dificultad == nivel
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (seleccionado) Color(0xFF00FF88) else Color(0xFF203c2e))
                        .clickable { viewModel.dificultad.value = nivel }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(icono, fontSize = 22.sp)
                        Text(
                            text = nivel,
                            color = if (seleccionado) Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = xpLabel,
                            color = if (seleccionado) Color(0xFF1a3a2a) else Color(0xFF00FF88),
                            fontSize = 11.sp
                        )
                    }
                }
                if (nivel != "HARD") Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ---- RPG Attribute Focus ----
        Text(
            text = "RPG ATTRIBUTE FOCUS",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        val atributos = listOf(
            Pair("Strength", "🏋️"),
            Pair("Intelligence", "🧠"),
            Pair("Agility", "🏃"),
            Pair("Charisma", "🗣️")
        )

        // Primera fila
        Row(modifier = Modifier.fillMaxWidth()) {
            atributos.take(2).forEach { (nombre, icono) ->
                val seleccionado = tipo == nombre
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (seleccionado) Color(0xFF00FF88) else Color(0xFF203c2e))
                        .clickable { viewModel.tipo.value = nombre }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(icono, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = nombre,
                            color = if (seleccionado) Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                if (nombre == "Strength") Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Segunda fila
        Row(modifier = Modifier.fillMaxWidth()) {
            atributos.drop(2).forEach { (nombre, icono) ->
                val seleccionado = tipo == nombre
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (seleccionado) Color(0xFF00FF88) else Color(0xFF203c2e))
                        .clickable { viewModel.tipo.value = nombre }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(icono, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = nombre,
                            color = if (seleccionado) Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                if (nombre == "Agility") Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---- Botón guardar ----
        Button(
            onClick = { viewModel.guardarHabito() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚔️", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BEGIN QUEST",
                    color = Color(0xFF1a3a2a),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}