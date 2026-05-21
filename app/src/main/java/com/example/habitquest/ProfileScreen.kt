package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onStatisticsClick: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // VIEWMODEL - Maneja la lógica del perfil
    val viewModel: ProfileViewModel = viewModel()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val editName by viewModel.editName.collectAsState()
    val editEmail by viewModel.editEmail.collectAsState()
    val editClass by viewModel.editClass.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Profile",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.weight(1f)
            )
            if (!isEditMode && currentUser != null) {
                IconButton(onClick = { viewModel.enableEditMode() }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF00FF88)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // MOSTRAR MENSAJE DE ERROR
        errorMessage?.let { error ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF3333), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        error,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "✕",
                        modifier = Modifier.clickable { viewModel.clearMessages() },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // MOSTRAR MENSAJE DE ÉXITO
        successMessage?.let { success ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00FF88), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        success,
                        color = Color(0xFF1a3a2a),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "✓",
                        modifier = Modifier.clickable { viewModel.clearMessages() },
                        color = Color(0xFF1a3a2a),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // CONTENIDO PRINCIPAL
        if (isLoading && currentUser == null) {
            // Loading inicial
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00FF88))
            }
        } else if (currentUser == null) {
            // Error al cargar
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Error al cargar perfil",
                        color = Color(0xFFFF3333),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* Reintentar carga */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88))
                    ) {
                        Text("Reintentar", color = Color(0xFF1a3a2a))
                    }
                }
            }
        } else {
            // CONTENIDO DEL PERFIL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // AVATAR Y NOMBRE PRINCIPAL
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF203c2e), RoundedCornerShape(18.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // AVATAR
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0d6b4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                viewModel.getClassIcon(currentUser!!.clase),
                                fontSize = 40.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // NOMBRE
                        if (isEditMode) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { viewModel.updateEditName(it) },
                                label = { Text("Nombre", color = Color(0xFF55ffb0)) },
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFF00FF88),
                                    focusedBorderColor = Color(0xFF00FF88),
                                    cursorColor = Color(0xFF00FF88),
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                ),
                                enabled = !isLoading
                            )
                        } else {
                            Text(
                                currentUser!!.nombre,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // CLASE
                        Text(
                            "${viewModel.getClassDisplayName(currentUser!!.clase)} CLASS",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // INFORMACIÓN DEL PERSONAJE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "CHARACTER INFO",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // NIVEL Y XP
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF00FF88), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "LVL ${viewModel.getCurrentLevel()}",
                                    color = Color(0xFF1a3a2a),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "XP Progress",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { viewModel.getLevelProgressPercentage() },
                                    color = Color(0xFF00FF88),
                                    trackColor = Color(0xFF2c4d3a),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "${viewModel.getXpInLevel()} / ${viewModel.getXpForNextLevel()} XP (${viewModel.getXpForNextLevel()} to next level)",
                                    color = Color(0xFF999999),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // ESTADÍSTICAS RPG
                        Text(
                            "RPG STATS",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem("Discipline", currentUser!!.disciplina, "🎯")
                            Spacer(modifier = Modifier.width(12.dp))
                            StatItem("Strength", currentUser!!.fuerza, "💪")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem("Intelligence", currentUser!!.inteligencia, "🧠")
                            Spacer(modifier = Modifier.width(12.dp))
                            StatItem("Consistency", currentUser!!.consistencia, "⚡")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // INFORMACIÓN DE CUENTA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "ACCOUNT INFO",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // CORREO
                        Text(
                            "Email",
                            color = Color(0xFF999999),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isEditMode) {
                            OutlinedTextField(
                                value = editEmail,
                                onValueChange = { viewModel.updateEditEmail(it) },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = Color.White,
                                    fontSize = 14.sp
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFF00FF88),
                                    focusedBorderColor = Color(0xFF00FF88),
                                    cursorColor = Color(0xFF00FF88),
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                ),
                                enabled = !isLoading
                            )
                        } else {
                            Text(
                                currentUser!!.correo,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // CLASE
                        Text(
                            "Class",
                            color = Color(0xFF999999),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isEditMode) {
                            // Selector de clase
                            Row(modifier = Modifier.fillMaxWidth()) {
                                viewModel.getAvailableClasses().forEach { userClass ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (editClass == userClass) Color(0xFF00FF88)
                                                else Color(0xFF2c4d3a)
                                            )
                                            .clickable(enabled = !isLoading) {
                                                viewModel.updateEditClass(userClass)
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                viewModel.getClassIcon(userClass),
                                                fontSize = 20.sp
                                            )
                                            Text(
                                                viewModel.getClassDisplayName(userClass),
                                                color = if (editClass == userClass) Color(0xFF1a3a2a) else Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    if (userClass != "ADVENTURER") Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    viewModel.getClassIcon(currentUser!!.clase),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    viewModel.getClassDisplayName(currentUser!!.clase),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // BOTONES DE ACCIÓN (si está en modo edición)
                if (isEditMode) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.cancelEdit() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF666666)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            Text(
                                "CANCEL",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = { viewModel.saveProfileChanges() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00FF88)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color(0xFF1a3a2a)
                                )
                            } else {
                                Text(
                                    "SAVE",
                                    color = Color(0xFF1a3a2a),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // TRES BOTONES PRINCIPALES
                Column(modifier = Modifier.fillMaxWidth()) {
                    // BOTÓN ESTADÍSTICAS
                    Button(
                        onClick = { onStatisticsClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF203c2e)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text("📊", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Statistics",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text("→", color = Color(0xFF00FF88), fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BOTÓN LOGROS
                    Button(
                        onClick = { onAchievementsClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF203c2e)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text("🏆", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Achievements",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text("→", color = Color(0xFF00FF88), fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BOTÓN CERRAR SESIÓN
                    Button(
                        onClick = {
                            viewModel.logout {
                                onLogout()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3333)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text("🚪", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Logout",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text("→", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.StatItem(label: String, value: Int, icon: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF2c4d3a), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    label,
                    color = Color(0xFF999999),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    value.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    HabitQuestTheme {
        ProfileScreen()
    }
}
