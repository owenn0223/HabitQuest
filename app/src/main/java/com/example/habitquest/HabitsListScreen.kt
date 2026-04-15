package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp

@Composable
fun HabitsListScreen(
    onBack: () -> Unit = {},
    onCreateHabit: () -> Unit = {},
    onAchievementsClick: () -> Unit = {}
) {
    val selectedFilter = remember { mutableStateOf("All") }
    val filters = listOf("All", "Daily", "Weekly", "Monthly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "HabitQuest",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00FF88), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ LVL 14", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Settings, contentDescription = "Profile", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // FILTROS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            filters.forEach { filter ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selectedFilter.value == filter) Color(0xFF00FF88)
                            else Color(0xFF203c2e)
                        )
                        .clickable { selectedFilter.value = filter }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (selectedFilter.value == filter) Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ACTIVE QUESTS LABEL
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ACTIVE QUESTS",
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "4 Remaining",
                color = Color(0xFF999999),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // LISTA DE HÁBITOS
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(4) { index ->
                HabitCard(
                    habitName = when (index) {
                        0 -> "Morning Meditation"
                        1 -> "Heavy Lifting Session"
                        2 -> "Drink 2L Water"
                        else -> "Read 20 Pages"
                    },
                    frequency = when (index) {
                        0 -> "DAILY"
                        1 -> "WEEKLY"
                        2 -> "DAILY"
                        else -> "DAILY"
                    },
                    difficulty = when (index) {
                        0 -> "EASY"
                        1 -> "HARD"
                        2 -> "EASY"
                        else -> "MED"
                    },
                    xp = when (index) {
                        0 -> "50 XP"
                        1 -> "250 XP"
                        2 -> "25 XP"
                        else -> "100 XP"
                    },
                    isCompleted = index == 3
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // FLOATING ACTION BUTTON
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 80.dp, end = 16.dp)
        ) {
            Button(
                onClick = { onCreateHabit() },
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88)),
                shape = CircleShape
            ) {
                Text("+", fontSize = 32.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        // BOTTOM NAVIGATION
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF203c2e), RoundedCornerShape(24.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Dashboard */ }) {
                Text("🏠", fontSize = 26.sp)
            }
            IconButton(onClick = { /* TODO: Quests */ }) {
                Text("⚔️", fontSize = 26.sp)
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00FF88)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onCreateHabit() }) {
                    Text("+", fontSize = 32.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = { /* TODO: Gear */ }) {
                Text("🎒", fontSize = 26.sp)
            }
            IconButton(onClick = { onAchievementsClick() }) {
                Text("👥", fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun HabitCard(
    habitName: String,
    frequency: String,
    difficulty: String,
    xp: String,
    isCompleted: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF203c2e), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habitName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Frequency badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (frequency) {
                                    "DAILY" -> Color(0xFF00FF88)
                                    "WEEKLY" -> Color(0xFF0088FF)
                                    else -> Color(0xFFFF8800)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = frequency,
                            color = if (frequency == "DAILY") Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Difficulty badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (difficulty) {
                                    "EASY" -> Color(0xFF00FF88)
                                    "MED" -> Color(0xFFFFAA00)
                                    "HARD" -> Color(0xFFFF3333)
                                    else -> Color(0xFF00FF88)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = difficulty,
                            color = if (difficulty == "EASY") Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // XP badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF0d6b4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡ ", fontSize = 10.sp)
                            Text(
                                text = xp,
                                color = Color(0xFF00FF88),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Checkbox/Completion button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isCompleted) Color(0xFF00FF88)
                        else Color(0xFF2c4d3a)
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Text("✓", fontSize = 28.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
                } else {
                Icon(
                    painter = painterResource(android.R.drawable.ic_input_add),
                    contentDescription = "Complete",
                    tint = Color(0xFF00FF88),
                    modifier = Modifier.size(24.dp)
                )
                }
            }
        }
    }
}

