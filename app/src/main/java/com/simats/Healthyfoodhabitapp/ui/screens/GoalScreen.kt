package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun GoalScreen(onStartClick: () -> Unit) {
    var selectedGoal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose your goal 🎯",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "What do you want to achieve?",
            fontSize = 18.sp,
            color = DarkGreen.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(40.dp))

        GoalCard(
            title = "Weight Loss",
            subtitle = "Burn fat & get lean",
            emoji = "🏃",
            emojiBgColor = Color(0xFFFFDAB9),
            isSelected = selectedGoal == "Weight Loss",
            onClick = { selectedGoal = "Weight Loss" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoalCard(
            title = "Weight Gain",
            subtitle = "Build muscle & strength",
            emoji = "💪",
            emojiBgColor = Color(0xFFD1E8FF),
            isSelected = selectedGoal == "Weight Gain",
            onClick = { selectedGoal = "Weight Gain" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoalCard(
            title = "Eat Healthy",
            subtitle = "Maintain & stay fit",
            emoji = "🥗",
            emojiBgColor = Color(0xFFC1F0D1),
            isSelected = selectedGoal == "Eat Healthy",
            onClick = { selectedGoal = "Eat Healthy" }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedGoal != null) DarkGreen else Color.Gray
            ),
            shape = RoundedCornerShape(32.dp),
            enabled = selectedGoal != null
        ) {
            Text(text = "Start My Journey", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GoalCard(
    title: String,
    subtitle: String,
    emoji: String,
    emojiBgColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = emojiBgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = DarkGreen.copy(alpha = 0.6f)
                )
            }
        }
    }
}
