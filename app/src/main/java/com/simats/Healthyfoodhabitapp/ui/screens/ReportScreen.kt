package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ReportScreen(
    navController: NavController,
    water: String,
    sleep: String,
    bedTime: String,
    wakeTime: String
) {
    val waterIntake = water.toIntOrNull() ?: 0
    val sleepHours = sleep.toDoubleOrNull() ?: 0.0
    
    // Analysis Logic
    val waterScore = (waterIntake / 8.0 * 100).coerceAtMost(100.0)
    val sleepScore = (sleepHours / 8.0 * 100).coerceAtMost(100.0)
    val totalScore = ((waterScore + sleepScore) / 2).roundToInt()

    val wellnessMessage = when {
        totalScore >= 90 -> "Outstanding! You are maintaining peak health habits. Keep this momentum! 🚀"
        totalScore >= 70 -> "Great job! You're on the right track, just a few small adjustments needed. 👍"
        totalScore >= 50 -> "Fair. Your habits are okay, but you might feel more energetic with more water and sleep. 📈"
        else -> "Needs Attention. Your body is asking for more care. Let's start improving today! 💪"
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_meal") },
                containerColor = Color.White,
                contentColor = DarkGreen,
                shape = CircleShape,
                modifier = Modifier.offset(y = 50.dp).size(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Header
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Analysis Results", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Score Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Wellness Score", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "$totalScore%", color = Color.White, fontSize = 56.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = wellnessMessage,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Water Intake Card
            item {
                val waterStatus = when {
                    waterIntake >= 8 -> "Perfect"
                    waterIntake >= 6 -> "Good"
                    else -> "Low"
                }
                val waterDesc = when {
                    waterIntake >= 8 -> "You've hit the recommended daily goal! Your body is well-hydrated. ✨"
                    waterIntake >= 6 -> "Nearly there! Just a couple more glasses to reach the ideal 2L target."
                    else -> "Dehydration alert! You only drank $waterIntake glasses. Aim for 8 glasses daily. ⚠️"
                }
                
                ResultAlertCard(
                    title = "Water: $waterStatus ($waterIntake glasses)",
                    description = waterDesc,
                    bgColor = if (waterIntake >= 8) Color(0xFFE8F5E9) else Color(0xFFFFF1F1),
                    iconColor = if (waterIntake >= 8) Color(0xFF4CAF50) else Color(0xFFEF5350),
                    icon = if (waterIntake >= 8) Icons.Default.CheckCircle else Icons.Default.Warning
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Sleep Card
            item {
                val sleepStatus = when {
                    sleepHours >= 7.5 -> "Excellent"
                    sleepHours >= 6.0 -> "Adequate"
                    else -> "Poor"
                }
                val formattedSleep = String.format(Locale.getDefault(), "%.1f", sleepHours)
                val sleepDesc = when {
                    sleepHours >= 7.5 -> "Perfect rest! You slept $formattedSleep hours. Your brain is fully charged. 😴"
                    sleepHours >= 6.0 -> "You got $formattedSleep hours. Try to aim for a full 8 hours tonight."
                    else -> "Sleep deprivation! $formattedSleep hours is not enough for recovery. Try to sleep earlier. 🌙"
                }

                ResultAlertCard(
                    title = "Sleep: $sleepStatus (${formattedSleep}h)",
                    description = sleepDesc,
                    bgColor = if (sleepHours >= 7.5) Color(0xFFE8F5E9) else Color(0xFFFFFDE7),
                    iconColor = if (sleepHours >= 7.5) Color(0xFF4CAF50) else Color(0xFFFBC02D),
                    icon = if (sleepHours >= 7.5) Icons.Default.CheckCircle else Icons.Default.Bedtime
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Recommendations
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(text = "Smart Suggestions", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (waterIntake < 8) {
                            RecommendationItem("💧", "Drink one glass of water every 2 hours to reach 8 glasses.")
                        }
                        if (sleepHours < 8) {
                            val bedtimeHour = bedTime.split(":")[0].toIntOrNull() ?: 22
                            RecommendationItem("🌙", "Move your bedtime to ${bedtimeHour - 1}:00 PM tonight.")
                        }
                        RecommendationItem("🧘", "Practice deep breathing for 5 minutes to boost oxygen levels.")
                        RecommendationItem("🥗", "Ensure your next meal has a high water content (like cucumber or watermelon).")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Save Report
            item {
                Button(
                    onClick = { 
                        navController.navigate("saved_report?water=$water&sleep=$sleep&bedTime=$bedTime&wakeTime=$wakeTime&score=$totalScore") 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0F0E8))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Download Full Analysis 📱", color = DarkGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
