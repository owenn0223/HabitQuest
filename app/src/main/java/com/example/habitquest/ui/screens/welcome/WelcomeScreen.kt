package com.example.habitquest.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitquest.ui.theme.HabitQuestTheme

@Composable
fun WelcomeScreen(
    onCreateCharacterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(140.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF00FF88),
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = Color(0xFF0d6b4f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✔✔",
                fontSize = 60.sp,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "HabitQuest",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Level up your life, one habit at a time",
            fontSize = 16.sp,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = { onLoginClick() },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF88)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Log In →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1a3a2a)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onCreateCharacterClick() },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF00FF88),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Create Character 👤",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF88)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "VERSION 1.0.0 · VARIANT 1 OF 10",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    HabitQuestTheme {
        WelcomeScreen(
            onCreateCharacterClick = {},
            onLoginClick = {}
        )
    }
}
