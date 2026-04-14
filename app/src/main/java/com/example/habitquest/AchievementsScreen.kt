package com.example.habitquest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitquest.ui.theme.HabitQuestTheme

@Composable
fun AchievementsScreen(
    onBack: () -> Unit = {}
) {
    val selectedTab = remember { mutableStateOf("Unlocked") }
    val tabs = listOf("Unlocked", "Locked")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Trophy Room",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { /* TODO: Share */ }) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // STATS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(label = "BADGES EARNED", value = "24", bgColor = Color(0xFF203c2e))
            StatCard(label = "TOTAL XP", value = "1,500", bgColor = Color(0xFF203c2e))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TABS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0d6b4f), RoundedCornerShape(10.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEach { tab ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selectedTab.value == tab) Color(0xFF00FF88)
                            else Color.Transparent
                        )
                        .clickable { selectedTab.value = tab }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (selectedTab.value == tab) Color(0xFF1a3a2a) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION TITLE
        Text(
            text = "Milestone Badges",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // BADGES GRID
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getAchievementsList()) { achievement ->
                AchievementBadgeRow(achievement = achievement, isUnlocked = selectedTab.value == "Unlocked")
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
            IconButton(onClick = { /* TODO: Home */ }) {
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
                IconButton(onClick = { /* TODO: Add */ }) {
                    Text("+", fontSize = 32.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = { /* TODO: Gear */ }) {
                Text("🎒", fontSize = 26.sp)
            }
            IconButton(onClick = { /* TODO: Badges */ }) {
                Text("👥", fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(16.dp)
            .widthIn(min = 140.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun AchievementBadgeRow(
    achievement: Achievement,
    isUnlocked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isUnlocked) Color(0xFF203c2e)
                else Color(0xFF0d3d2a)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Badge Icon
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (isUnlocked) Color(0xFF00FF88).copy(alpha = 0.3f)
                    else Color(0xFF666666).copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = achievement.icon,
                fontSize = 28.sp,
                color = if (isUnlocked) Color(0xFF00FF88) else Color(0xFF666666)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = achievement.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = achievement.description,
                color = Color(0xFF999999),
                fontSize = 12.sp
            )
        }

        // Lock/Unlock Icon
        if (!isUnlocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00FF88)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 16.sp, color = Color(0xFF1a3a2a), fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Achievement(
    val name: String,
    val description: String,
    val icon: String
)

fun getAchievementsList(): List<Achievement> {
    return listOf(
        Achievement(
            name = "First Habit",
            description = "DAY 1 WARRIOR",
            icon = "💡"
        ),
        Achievement(
            name = "7 Day Streak",
            description = "THE CONSISTENCY",
            icon = "🔥"
        ),
        Achievement(
            name = "Social Butterfly",
            description = "GROUP QUEST",
            icon = "🦋"
        ),
        Achievement(
            name = "Early Bird",
            description = "RISE & GRIND",
            icon = "🌅"
        ),
        Achievement(
            name = "30 Day Streak",
            description = "LEGENDARY",
            icon = "👑"
        ),
        Achievement(
            name = "Master Planner",
            description = "ORGANIZATION",
            icon = "📊"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AchievementsScreenPreview() {
    HabitQuestTheme {
        AchievementsScreen()
    }
}
