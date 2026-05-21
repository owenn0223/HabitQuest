package com.example.habitquest.ui.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitquest.viewmodel.AchievementsViewModel
import com.example.habitquest.network.ApiAchievement

@Composable
fun AchievementsScreen(
    onBack: () -> Unit = {},
    // Inyectamos el ViewModel (asegúrate de tener un Provider o Factory si es necesario)
    viewModel: AchievementsViewModel = viewModel()
) {
    val achievements by viewModel.achievements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val selectedTab = remember { mutableStateOf("Unlocked") }
    val tabs = listOf("Unlocked", "Locked")

    // Filtrar logros según el tab seleccionado
    val filteredAchievements = achievements.filter {
        if (selectedTab.value == "Unlocked") it.unlocked else !it.unlocked
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        // ... (Mantener Header igual)

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00FF88))
            }
        } else if (error != null) {
            Text(text = error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
        } else {
            // TABS y LISTA
            // ... (Código de Tabs igual)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredAchievements) { achievement ->
                    AchievementBadgeRow(
                        name = achievement.name,
                        description = achievement.description,
                        isUnlocked = achievement.unlocked
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementBadgeRow(name: String, description: String, isUnlocked: Boolean) {
    // Versión simplificada para el ejemplo
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isUnlocked) Color(0xFF203c2e) else Color(0xFF0d3d2a),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = if (isUnlocked) "🏆" else "🔒", fontSize = 30.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = name, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = description, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

