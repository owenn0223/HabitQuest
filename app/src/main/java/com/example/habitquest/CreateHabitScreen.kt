package com.example.habitquest

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
import com.example.habitquest.ui.theme.HabitQuestTheme

@Composable
fun CreateHabitScreen(
    onBack: () -> Unit = {},
    onBeginQuest: () -> Unit = {}
) {
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
        // Quest Details
        Text(
            text = "QUEST DETAILS",
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
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
            )
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF00FF88))
                    .clickable { }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("DAILY", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("WEEKLY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("😊", fontSize = 22.sp)
                    Text("EASY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF00FF88))
                    .clickable { }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⚡", fontSize = 22.sp, color = Color(0xFF1a3a2a))
                    Text("MEDIUM", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💀", fontSize = 22.sp)
                    Text("HARD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
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
                    .background(Color(0xFF00FF88))
                    .clickable { }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏋️", fontSize = 18.sp, color = Color(0xFF1a3a2a))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Strength", color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧠", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Intelligence", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏃", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Agility", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF203c2e))
                    .clickable { }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🗣️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Charisma", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        // Begin Quest Button
        Button(
            onClick = { onBeginQuest() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88)),
            shape = RoundedCornerShape(14.dp)
        ) {
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

