package com.example.habitquest.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.ui.screens.dashboard.DashboardViewModel

@Composable
fun StatisticsScreen(
    onBack: () -> Unit = {}
) {
    // Usamos el mismo ViewModel del Dashboard para obtener estadísticas
    val viewModel: DashboardViewModel = viewModel()

    // Estados del ViewModel
    val habitsToday by viewModel.habitsToday.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val totalXP by viewModel.totalXP.collectAsState()
    val level by viewModel.level.collectAsState()
    val xpInLevel by viewModel.xpInLevel.collectAsState()
    val xpForNextLevel by viewModel.xpForNextLevel.collectAsState()
    val xpProgress by viewModel.xpProgress.collectAsState()

    // Nuevos estados para estadísticas
    val habitsCreated by viewModel.habitsCreated.collectAsState()
    val habitsCompleted by viewModel.habitsCompleted.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()

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
                text = "Statistics",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // NIVEL Y PROGRESO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF203c2e), RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "LEVEL PROGRESS",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // NIVEL GRANDE
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0d6b4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "LVL",
                                color = Color(0xFF00FF88),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                level.toString(),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // BARRA DE PROGRESO
                    Text(
                        "XP Progress to Level ${level + 1}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { xpProgress },
                        color = Color(0xFF00FF88),
                        trackColor = Color(0xFF2c4d3a),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "$xpInLevel / $xpForNextLevel XP",
                        color = Color(0xFF999999),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ESTADÍSTICAS PRINCIPALES
            Row(modifier = Modifier.fillMaxWidth()) {
                // HÁBITOS HOY
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0d6b4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📅", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Habits Today",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            habitsToday,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // RACHA ACTUAL
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0d6b4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔥", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Current Streak",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "$streak Days",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // XP TOTAL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0d6b4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🪙", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Total Lifetime XP",
                            color = Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "%,d".format(totalXP),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Keep building your habit empire!",
                            color = Color(0xFF999999),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ESTADÍSTICAS ADICIONALES
            Text(
                "ACHIEVEMENT STATS",
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // GRID DE ESTADÍSTICAS
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        icon = "🎯",
                        title = "Habits Created",
                        value = habitsCreated.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        icon = "✅",
                        title = "Habits Completed",
                        value = habitsCompleted.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        icon = "🏆",
                        title = "Achievements",
                        value = achievements.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        icon = "📈",
                        title = "Best Streak",
                        value = "$bestStreak Days",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // MOTIVACIÓN
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💪", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Keep pushing forward!",
                        color = Color(0xFF00FF88),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Every habit you build makes you stronger",
                        color = Color(0xFF999999),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun StatCard(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFF203c2e), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0d6b4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StatisticsScreenPreview() {
    HabitQuestTheme {
        StatisticsScreen()
    }
}
