package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onCreateHabitClick: () -> Unit = {},
    onHabitsListClick: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    // ✅ ViewModel conectado a Room
    val viewModel: DashboardViewModel = viewModel()

    // ✅ Datos reactivos desde la base de datos
    val habitsToday by viewModel.habitsToday.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val totalXP by viewModel.totalXP.collectAsState()
    val level by viewModel.level.collectAsState()
    val xpInLevel by viewModel.xpInLevel.collectAsState()
    val xpForNextLevel by viewModel.xpForNextLevel.collectAsState()
    val xpProgress by viewModel.xpProgress.collectAsState()
    val currentQuest by viewModel.currentQuest.collectAsState()

    // ✅ Datos del usuario desde SesionManager
    val userName by viewModel.userName.collectAsState()
    val userClass by viewModel.userClass.collectAsState()

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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0d6b4f)),
                contentAlignment = Alignment.Center
            ) {
                Text("✔", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "HabitQuest",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* TODO: Notificaciones */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
            }
            IconButton(onClick = { onProfileClick() }) {
                Icon(Icons.Default.Settings, contentDescription = "Profile", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // CARD DE USUARIO — nivel y XP dinámicos
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0d6b4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (userClass.uppercase()) {
                            "WARRIOR" -> "⚔️"
                            "MAGE" -> "🔮"
                            "SAGE" -> "📚"
                            "ADVENTURER" -> "🗺️"
                            else -> "🛡️"
                        },
                        fontSize = 28.sp
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        userName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "$userClass CLASS",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // ✅ Nivel dinámico
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF00FF88), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "LVL $level",
                                color = Color(0xFF1a3a2a),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "XP to Level ${level + 1}",
                            color = Color(0xFF999999),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // ✅ XP dinámico
                        Text(
                            "$xpInLevel / $xpForNextLevel",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    // ✅ Barra de progreso dinámica
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        color = Color(0xFF00FF88),
                        trackColor = Color(0xFF2c4d3a),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ESTADÍSTICAS — datos dinámicos
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Habits Today",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // ✅ Hábitos completados/total dinámico
                    Text(
                        habitsToday,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Current Streak",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // ✅ Racha dinámica
                    Text(
                        "$streak Days",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // XP TOTAL — dinámico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Total Lifetime XP",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // ✅ XP total dinámico
                    Text(
                        "%,d".format(totalXP),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00FF88)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🪙", fontSize = 18.sp, color = Color(0xFF1a3a2a))
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Current Quest",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // CURRENT QUEST — dinámico desde Room
        if (currentQuest != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0d6b4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚔️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        // ✅ Nombre del hábito pendiente real
                        Text(
                            currentQuest!!.nombre,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // ✅ XP real del hábito
                            Text(
                                "+${currentQuest!!.xp} XP",
                                color = Color(0xFF00FF88),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // ✅ Frecuencia real
                            Text(
                                currentQuest!!.frecuencia,
                                color = Color(0xFF999999),
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // ✅ Botón que marca el hábito como completado
                    IconButton(
                        onClick = { viewModel.completeCurrentQuest() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_media_play),
                            contentDescription = "Completar",
                            tint = Color(0xFF00FF88),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        } else {
            // ✅ Mensaje cuando todos los hábitos del día están completos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¡Todos los hábitos completados!",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // GUILD BLOQUEADO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF00FF88), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF203c2e)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔒", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    // ✅ Muestra el nivel real requerido vs el actual
                    if (level >= 15)
                        "¡Guild desbloqueado! Únete a una Guild para raids semanales."
                    else
                        "Join a Guild at Level 15 to unlock collaborative raids and weekly rewards! (LVL $level/15)",
                    color = Color(0xFF999999),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM NAVIGATION
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(24.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Dashboard actual */ }) {
                Text("🏠", fontSize = 26.sp)
            }
            IconButton(onClick = { onHabitsListClick() }) {
                Text("⚔️", fontSize = 26.sp)
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00FF88)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onCreateHabitClick() }) {
                    Text("+", fontSize = 32.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = { /* TODO: Items */ }) {
                Text("🎒", fontSize = 26.sp)
            }
            IconButton(onClick = { onProfileClick() }) {
                Text("👥", fontSize = 26.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun DashboardScreenPreview() {
    HabitQuestTheme {
        DashboardScreen()
    }
}