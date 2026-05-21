package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.model.Habit
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.viewmodel.HabitsListViewModel
import androidx.compose.runtime.mutableStateOf


@Composable
fun HabitsListScreen(
    onBack: () -> Unit = {},
    onCreateHabit: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    // VIEWMODEL - Conectar con Room Database
    val viewModel: HabitsListViewModel = viewModel()
    val habits by viewModel.habits.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    val remainingHabits by viewModel.remainingHabits.collectAsState()
    val userLevel by viewModel.userLevel.collectAsState()

    // Estado para diálogo de confirmación de eliminación
    val showDeleteDialog = remember { mutableStateOf(false) }
    val habitToDelete = remember { mutableStateOf<Habit?>(null) }

    // Estado local para filtros (temporal, luego se moverá a ViewModel)
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
                    Text("⚡ LVL $userLevel", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                            if (currentFilter == filter) Color(0xFF00FF88)
                            else Color(0xFF203c2e)
                        )
                        .clickable { viewModel.setFilter(filter) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (currentFilter == filter) Color(0xFF1a3a2a) else Color.White,
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
                text = "${remainingHabits} Remaining",
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
            items(habits) { habit ->
                HabitCard(
                    habit = habit,
                    onCompleteClick = { viewModel.toggleHabitCompletion(habit.id) },
                    onDeleteClick = {
                        habitToDelete.value = habit
                        showDeleteDialog.value = true
                    }
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
            IconButton(onClick = { onBack() }) {
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
            IconButton(onClick = { onProfileClick() }) {
                Text("👥", fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Diálogo de confirmación de eliminación
        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                title = { Text("Confirmar Eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar este hábito?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            habitToDelete.value?.let { viewModel.deleteHabit(it.id) }
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog.value = false }) {
                        Text("Cancelar")
                    }
                },
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    onCompleteClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
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
                    text = habit.nombre,
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
                                color = when (habit.frecuencia) {
                                    "DAILY" -> Color(0xFF00FF88)
                                    "WEEKLY" -> Color(0xFF0088FF)
                                    else -> Color(0xFFFF8800)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = habit.frecuencia,
                            color = if (habit.frecuencia == "DAILY") Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Difficulty badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (habit.dificultad) {
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
                            text = habit.dificultad,
                            color = if (habit.dificultad == "EASY") Color(0xFF1a3a2a) else Color.White,
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
                                text = habit.xp.toString(),
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
                        if (habit.completado) Color(0xFF00FF88)
                        else Color(0xFF2c4d3a)
                    )
                    .clickable { onCompleteClick() },
                contentAlignment = Alignment.Center
            ) {
                if (habit.completado) {
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

            // Delete button
            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFF3333),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitsListScreenPreview() {
    HabitQuestTheme {
        HabitsListScreen()
    }
}
