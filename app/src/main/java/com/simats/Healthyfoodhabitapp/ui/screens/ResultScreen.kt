package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun ResultScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
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

            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = { navController.popBackStack() },
                        shape = CircleShape,
                        color = Color(0xFFF0F2F5).copy(alpha = 0.5f),
                        modifier = Modifier.size(45.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = DarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = "Wellness Results 📊",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f).padding(end = 45.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Wellness Score Card
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
                        Text(text = "70%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Not bad, but there's room for improvement. Focus on the areas highlighted above for a healthier day! 💪",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Water Intake Result
            item {
                ResultAlertCard(
                    title = "Water Intake — Too Low",
                    description = "Only 4 glasses! Your body needs much more water. Dehydration can cause headaches and fatigue. Drink more water! ⚠️",
                    bgColor = Color(0xFFFFF1F1),
                    iconColor = Color(0xFFEF5350),
                    icon = Icons.Default.Info
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Sleep Quality Result
            item {
                ResultAlertCard(
                    title = "Sleep Quality — Could Be Better",
                    description = "You slept 7.5 hours. It's okay but aim for 8 hours for better recovery and energy. 😴",
                    bgColor = Color(0xFFFFFDE7),
                    iconColor = Color(0xFFFBC02D),
                    icon = Icons.Default.Info
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Quick Recommendations
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = DarkGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Quick Recommendations", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        RecommendationItem("💧", "Increase water intake to at least 8 glasses (2L) daily. Set hourly reminders!")
                        RecommendationItem("🌙", "Aim for 7-9 hours of sleep. Try going to bed by 10:30 PM and avoid screens 1 hour before.")
                        RecommendationItem("🥗", "Eat balanced meals with fruits, vegetables, and protein throughout the day.")
                        RecommendationItem("🚶", "Take a 15-minute walk after meals to improve digestion and energy levels.")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Save Report to Device Button
            item {
                Button(
                    onClick = { navController.navigate("report") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0F0E8))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Save Report to Device 📱", color = DarkGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
