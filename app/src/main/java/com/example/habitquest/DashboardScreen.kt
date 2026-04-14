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
import com.example.habitquest.ui.theme.HabitQuestTheme

@Composable
fun DashboardScreen(
    onCreateHabitClick: () -> Unit = {},
    onHabitsListClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo circular
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
            IconButton(onClick = { /* TODO: Settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // Card de usuario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Foto de perfil
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0d6b4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🛡️", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Ironclad Guardian", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("WARRIOR CLASS", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF00FF88), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("LVL 12", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("XP to Level 13", color = Color(0xFF999999), fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("450 / 1000", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = 0.45f,
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
        // Estadísticas
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Habits Today", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("4/8", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
                    Text("Current Streak", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("15 Days", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // XP
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total Lifetime XP", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("12,450", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
        // Current Quest
        Text(
            text = "Current Quest",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
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
                    Text("Morning Vitality", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Perform 20 minutes of cardio", color = Color(0xFF999999), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("+50 XP", color = Color(0xFF00FF88), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("08:30 AM", color = Color(0xFF999999), fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* TODO: Play quest */ }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_media_play),
                        contentDescription = "Play",
                        tint = Color(0xFF00FF88),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Guild bloqueado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFF00FF88),
                    shape = RoundedCornerShape(14.dp)
                )
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
                    text = "Join a Guild at Level 15 to unlock collaborative raids and weekly rewards!",
                    color = Color(0xFF999999),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // Bottom Navigation
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(24.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Dash */ }) {
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
            IconButton(onClick = { /* TODO: Social */ }) {
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
