package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun HealthReportScreen(navController: NavController) {
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
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
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
                        text = "Health Report 📈",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f).padding(end = 45.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Health Score Card (Centered Content)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Your Health Score", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 0.61f },
                                modifier = Modifier.size(140.dp),
                                color = Color(0xFF4CAF50),
                                strokeWidth = 12.dp,
                                trackColor = Color.White.copy(alpha = 0.1f),
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "61%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Good 👍", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+5% this week", color = Color(0xFF4CAF50), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Score Breakdown
            item {
                SectionHeader("Score Breakdown")
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        ScoreBar("Balanced Meals", 0.78f, Color(0xFF00BFA5), "78 %")
                        ScoreBar("Nutrient Coverage", 0.65f, Color(0xFF4285F4), "65 %")
                        ScoreBar("Junk Control", 0.82f, Color(0xFFFFA000), "82 %")
                        ScoreBar("Fruit & Veg", 0.58f, Color(0xFF7E57C2), "58 %")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Suggestions
            item {
                ResultAlertCard(
                    title = "Improve Your Score",
                    description = "Add more fruits and vegetables to your meals. Aim for at least 5 servings daily! 🍎🥦",
                    bgColor = Color(0xFFFFF1F1),
                    iconColor = Color(0xFFEF5350),
                    icon = Icons.Default.Info
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                ResultAlertCard(
                    title = "Keep It Up!",
                    description = "Your junk control is excellent. Stay consistent with your Healthy Diet journey! 🌟",
                    bgColor = Color(0xFFE8F5E9),
                    iconColor = Color(0xFF4CAF50),
                    icon = Icons.Default.CheckCircle
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Recommendations
            item {
                Text(
                    text = "Eat This for Healthy Diet 🥗",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FoodCard("Mediterranean Bowl", "380 kcal", "🥗", modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        FoodCard("Grilled Fish Tacos", "320 kcal", "🌮", modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FoodCard("Lentil Soup", "250 kcal", "🥣", modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        FoodCard("Fruit & Granola Bowl", "280 kcal", "🍓", modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Log Meal Button
            item {
                Button(
                    onClick = { navController.navigate("add_meal") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Log a Meal to Improve Score 🍽️", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun ScoreBar(label: String, progress: Float, color: Color, percent: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
            Text(text = percent, fontSize = 14.sp, color = DarkGreen.copy(alpha = 0.6f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF0F2F5)
        )
    }
}

@Composable
fun FoodCard(title: String, kcal: String, emoji: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFF0F2F5)),
                contentAlignment = Alignment.Center
            ) {
                // Since actual images aren't in res/drawable yet, using emoji as placeholder in a styled box
                // To use actual images, replace with:
                // Image(painter = painterResource(id = R.drawable.image_name), contentDescription = null, contentScale = ContentScale.Crop)
                Text(text = emoji, fontSize = 48.sp)
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGreen, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = kcal, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
