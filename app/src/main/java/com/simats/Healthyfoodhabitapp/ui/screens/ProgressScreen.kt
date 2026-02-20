package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavController) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val dateString = dateFormatter.format(Date(selectedDateMillis))
    
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDateMillis = it
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = DarkGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = DarkGreen)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
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
                .padding(horizontal = 24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Progress 📈",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    
                    Surface(
                        onClick = { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF0F2F5).copy(alpha = 0.5f),
                        modifier = Modifier.height(40.dp).width(120.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = dateString,
                                color = DarkGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = DarkGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Today's Health Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Today's Health", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "72%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "+8% better", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "vs yesterday", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                            }
                        }
                        
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 0.72f },
                                modifier = Modifier.size(80.dp),
                                color = Color(0xFF4CAF50),
                                strokeWidth = 8.dp,
                                trackColor = Color.White.copy(alpha = 0.1f),
                                strokeCap = StrokeCap.Round
                            )
                            Text(text = "🌿", fontSize = 24.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Today's Meal Breakdown Section
            item {
                SectionHeader("Today's Meal Breakdown")
                Spacer(modifier = Modifier.height(12.dp))
                
                // Meal List
                MealProgressItem("breakfast", "egg and tea", 0.52f, 0.44f, 0.88f, Color(0xFF4285F4))
                Spacer(modifier = Modifier.height(12.dp))
                MealProgressItem("lunch", "fish fry", 0.61f, 0.42f, 0.75f, Color(0xFF4285F4))
                Spacer(modifier = Modifier.height(12.dp))
                MealProgressItem("dinner", "chicken 65", 0.63f, 0.56f, 0.45f, Color(0xFF4285F4))
                Spacer(modifier = Modifier.height(12.dp))
                MealProgressItem("snack", "pancakes", 0.62f, 0.37f, 0.82f, Color(0xFF4285F4))
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Weekly Health Score Trend
            item {
                SectionHeader("Weekly Health Score Trend")
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        WeeklyTrendChart()
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("T", "W", "T", "F", "S", "S", "M").forEach { day ->
                                Text(text = day, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Nutrient Breakdown
            item {
                SectionHeader("Nutrient Breakdown")
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        NutrientItem("Protein", 0.58f, Color(0xFF4285F4), "58 %")
                        NutrientItem("Carbs & Fiber", 0.61f, Color(0xFFFF9800), "61 %")
                        NutrientItem("Minerals", 0.62f, Color(0xFF4CAF50), "62 %")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // AI Coach Suggestions
            item {
                SectionHeader("AI Coach Suggestions")
                Spacer(modifier = Modifier.height(12.dp))
                CoachSuggestionItem(
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF4CAF50),
                    title = "Great Protein Intake!",
                    description = "You're at 58% protein — hitting your goals. Keep it up! 💪",
                    tag = "muscle-building",
                    bgColor = Color(0xFFE8F5E9)
                )
                Spacer(modifier = Modifier.height(12.dp))
                CoachSuggestionItem(
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF4CAF50),
                    title = "Balanced Carbs!",
                    description = "Fiber & carbs at 61% — well balanced for sustained energy throughout the day! 🔋",
                    bgColor = Color(0xFFE8F5E9)
                )
                Spacer(modifier = Modifier.height(12.dp))
                CoachSuggestionItem(
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF4CAF50),
                    title = "Good Mineral Levels!",
                    description = "Minerals at 62% — your body is getting the micronutrients it needs! 🌿",
                    bgColor = Color(0xFFE8F5E9)
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun MealProgressItem(title: String, subtitle: String, score: Float, protein: Float, carbs: Float, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                    Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${(score * 100).toInt()} %",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Protein", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.width(50.dp))
                LinearProgressIndicator(
                    progress = { protein },
                    modifier = Modifier.weight(1f).height(4.dp).clip(CircleShape),
                    color = color,
                    trackColor = Color(0xFFF0F2F5)
                )
                Text(text = "${(protein * 100).toInt()}%", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Carbs", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.width(50.dp))
                LinearProgressIndicator(
                    progress = { carbs },
                    modifier = Modifier.weight(1f).height(4.dp).clip(CircleShape),
                    color = Color(0xFFFF9800),
                    trackColor = Color(0xFFF0F2F5)
                )
                Text(text = "${(carbs * 100).toInt()}%", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun WeeklyTrendChart() {
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val points = listOf(40f, 50f, 35f, 75f, 65f, 30f, 50f)
        val maxVal = 100f
        val width = size.width
        val height = size.height
        val spacing = width / (points.size - 1)

        val path = Path().apply {
            points.forEachIndexed { i, p ->
                val x = i * spacing
                val y = height - (p / maxVal * height)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = DarkGreen,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        points.forEachIndexed { i, p ->
            val x = i * spacing
            val y = height - (p / maxVal * height)
            drawCircle(color = DarkGreen, radius = 4.dp.toPx(), center = Offset(x, y))
        }
    }
}

@Composable
fun NutrientItem(label: String, progress: Float, color: Color, percent: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
            Text(text = percent, fontSize = 14.sp, color = Color.Gray)
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
fun CoachSuggestionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    tag: String? = null,
    bgColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 14.sp)
                    if (tag != null) {
                        Text(text = tag, fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, color = DarkGreen.copy(alpha = 0.8f), fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
    }
}
