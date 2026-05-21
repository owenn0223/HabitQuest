package com.example.habitquest.ui.screens.createhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.ui.screens.createhabit.CreateHabitViewModel

@Composable
fun CreateHabitScreen(
    onBack: () -> Unit = {},
    onBeginQuest: () -> Unit = {}
) {
    // VIEWMODEL - Maneja la lógica de creación de hábitos
    val viewModel: CreateHabitViewModel = viewModel()
    val habitName by viewModel.habitName.collectAsState()
    val selectedFrequency by viewModel.selectedFrequency.collectAsState()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val selectedAttribute by viewModel.selectedAttribute.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack() }) {
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
                    Text(error, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(
                        "✕",
                        modifier = Modifier.clickable { viewModel.clearErrorMessage() },
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
                    Text(success, color = Color(0xFF1a3a2a), fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(
                        "✓",
                        modifier = Modifier.clickable { viewModel.clearSuccessMessage() },
                        color = Color(0xFF1a3a2a),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Quest Details
        Text(
            text = "QUEST DETAILS",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = habitName,
            onValueChange = { viewModel.setHabitName(it) },
            placeholder = { Text("e.g., Morning Meditation", color = Color(0xFF55ffb0)) },
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF00FF88),
                focusedBorderColor = Color(0xFF00FF88),
                cursorColor = Color(0xFF00FF88),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Frequency
        Text(
            text = "FREQUENCY",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("DAILY", "WEEKLY", "MONTHLY").forEach { frequency ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (selectedFrequency == frequency) Color(0xFF00FF88)
                            else Color(0xFF203c2e)
                        )
                        .clickable(enabled = !isLoading) { viewModel.setFrequency(frequency) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        frequency,
                        color = if (selectedFrequency == frequency) Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                if (frequency != "MONTHLY") Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // Difficulty
        Text(
            text = "DIFFICULTY LEVEL",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            val difficulties = listOf(
                Triple("EASY", "😊", 10),
                Triple("MED", "⚡", 20),
                Triple("HARD", "💀", 40)
            )
            difficulties.forEach { (difficulty, emoji, xp) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (selectedDifficulty == difficulty) Color(0xFF00FF88)
                            else Color(0xFF203c2e)
                        )
                        .clickable(enabled = !isLoading) { viewModel.setDifficulty(difficulty) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(emoji, fontSize = 22.sp)
                        Text(
                            if (difficulty == "MED") "MEDIUM" else difficulty,
                            color = if (selectedDifficulty == difficulty) Color(0xFF1a3a2a) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            "$xp XP",
                            color = if (selectedDifficulty == difficulty) Color(0xFF1a3a2a) else Color(0xFF00FF88),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
                if (difficulty != "HARD") Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // RPG Attribute Focus
        Text(
            text = "RPG ATTRIBUTE FOCUS",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedAttribute == "Strength") Color(0xFF00FF88)
                        else Color(0xFF203c2e)
                    )
                    .clickable(enabled = !isLoading) { viewModel.setAttribute("Strength") }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏋️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Strength",
                        color = if (selectedAttribute == "Strength") Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedAttribute == "Intelligence") Color(0xFF00FF88)
                        else Color(0xFF203c2e)
                    )
                    .clickable(enabled = !isLoading) { viewModel.setAttribute("Intelligence") }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧠", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Intelligence",
                        color = if (selectedAttribute == "Intelligence") Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedAttribute == "Agility") Color(0xFF00FF88)
                        else Color(0xFF203c2e)
                    )
                    .clickable(enabled = !isLoading) { viewModel.setAttribute("Agility") }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏃", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Agility",
                        color = if (selectedAttribute == "Agility") Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedAttribute == "Charisma") Color(0xFF00FF88)
                        else Color(0xFF203c2e)
                    )
                    .clickable(enabled = !isLoading) { viewModel.setAttribute("Charisma") }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🗣️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Charisma",
                        color = if (selectedAttribute == "Charisma") Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        // Begin Quest Button
        Button(
            onClick = {
                viewModel.createHabit {
                    // Callback cuando se crea exitosamente
                    onBeginQuest()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF88),
                disabledContainerColor = Color(0xFF00AA55)
            ),
            shape = RoundedCornerShape(14.dp),
            enabled = !isLoading && habitName.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF1a3a2a),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CREATING...",
                    color = Color(0xFF1a3a2a),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚔️", fontSize = 22.sp, color = Color(0xFF1a3a2a))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BEGIN QUEST",
                        color = Color(0xFF1a3a2a),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun CreateHabitScreenPreview() {
    HabitQuestTheme {
        CreateHabitScreen()
    }
}
