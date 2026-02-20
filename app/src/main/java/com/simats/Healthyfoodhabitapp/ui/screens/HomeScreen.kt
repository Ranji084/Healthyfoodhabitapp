package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()) }
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
                onClick = { navController.navigate("add_meal?mealType=Breakfast") },
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
                    Column {
                        Text(
                            text = "Hi,  ranji ! 👋",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Text(
                            text = "Ready to crush it today?",
                            fontSize = 14.sp,
                            color = DarkGreen.copy(alpha = 0.7f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.FileDownload,
                                contentDescription = "Export",
                                tint = DarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Date Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F2F5).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFC1F0D1).copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            val isToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date()) == 
                                          SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(selectedDateMillis))
                            Text(text = if (isToday) "Today" else "Selected Day", fontSize = 12.sp, color = Color.Gray)
                            Text(text = dateString, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Daily Health Score Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Daily Health Score", color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 0.65f },
                                modifier = Modifier.size(150.dp),
                                color = Color(0xFF00C853),
                                strokeWidth = 14.dp,
                                trackColor = Color.White.copy(alpha = 0.1f),
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "65%", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Good Job!", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            StatItem("1,240", "Kcal Left")
                            StatItem("45g", "Protein")
                            StatItem("120g", "Carbs")
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Water and Steps
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SmallStatCard(
                        modifier = Modifier.weight(1f),
                        icon = "💧",
                        value = "1.2L",
                        label = "Water",
                        bgColor = Color(0xFFE3F2FD)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    SmallStatCard(
                        modifier = Modifier.weight(1f),
                        icon = "👟",
                        value = "4,502",
                        label = "Steps",
                        bgColor = Color(0xFFFFF3E0)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Health Report
            item {
                ActionCard(
                    icon = Icons.Default.FavoriteBorder,
                    title = "Health Report",
                    subtitle = "See how healthy you are & what to eat.",
                    onClick = { navController.navigate("health_report") }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Today's Insights
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("progress") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 0.65f },
                                modifier = Modifier.fillMaxSize(),
                                color = DarkGreen,
                                strokeWidth = 4.dp,
                                trackColor = Color(0xFFF0F2F5),
                                strokeCap = StrokeCap.Round
                            )
                            Text(text = "65%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Today's Insights", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                            Text(text = "See your full nutrition report", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text(text = "View", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = DarkGreen)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Today's Meals
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "Today's Meals", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                    Text(
                        text = "Add Meal",
                        fontSize = 14.sp,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate("add_meal") }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { MealItem("🍳", "Breakfast", "Log your meal", onClick = { navController.navigate("add_meal?mealType=Breakfast") }) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { MealItem("🥗", "Lunch", "Log your meal", onClick = { navController.navigate("add_meal?mealType=Lunch") }) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { MealItem("🍛", "Dinner", "Log your meal", onClick = { navController.navigate("add_meal?mealType=Dinner") }) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { MealItem("🍎", "Snack", "Log your meal", onClick = { navController.navigate("add_meal?mealType=Snack") }) }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
