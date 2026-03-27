package com.simats.Healthyfoodhabitapp.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.SessionManager
import com.simats.Healthyfoodhabitapp.network.*
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProgressScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    var insightsData by remember { mutableStateOf<InsightsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (userId == -1) {
            isLoading = false
            return@LaunchedEffect
        }

        val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
        val request = InsightsRequest(userId)
        
        api.getViewInsights(request).enqueue(object : Callback<InsightsResponse> {
            override fun onResponse(call: Call<InsightsResponse>, response: Response<InsightsResponse>) {
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    insightsData = response.body()
                    
                    // Sync health score to session for Home Screen
                    val score = insightsData?.healthPercentage ?: 0
                    val believableScore = if (score >= 100) 98 else score
                    sessionManager.saveHealthScore(believableScore)
                    sessionManager.saveAiAdvice(insightsData?.aiSuggestion ?: "Keep logging your meals!")
                } else {
                    // For new users, if the server returns 404 or empty, we treat it as "0 progress"
                    insightsData = InsightsResponse(healthPercentage = 0)
                }
            }

            override fun onFailure(call: Call<InsightsResponse>, t: Throwable) {
                isLoading = false
                // Default to 0 values on connection failure for new users
                insightsData = InsightsResponse(healthPercentage = 0)
            }
        })
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkGreen)
            }
        } else {
            val data = insightsData ?: InsightsResponse(healthPercentage = 0)
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item { Text("Your Insights 📊", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                
                // TODAY'S HEALTH PERCENTAGE
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Today's Health", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                val displayScore = if (data.healthPercentage >= 100) 98 else data.healthPercentage
                                Text("$displayScore%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Daily Progress", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                }
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                                CircularProgressIndicator(
                                    progress = { data.healthPercentage / 100f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF00C853),
                                    strokeWidth = 8.dp,
                                    trackColor = Color.White.copy(alpha = 0.1f),
                                    strokeCap = StrokeCap.Round
                                )
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
                item { Text("Today's Meals Breakdown", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                
                val meals = data.mealBreakdown ?: emptyMap()
                if (meals.isEmpty()) {
                    item {
                        Text("No meals logged today. Log your breakfast to see your progress!", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(vertical = 16.dp))
                    }
                } else {
                    val order = listOf("breakfast", "lunch", "snacks", "snack", "dinner")
                    val sortedTypes = meals.keys.sortedBy { type -> 
                        val idx = order.indexOf(type.lowercase())
                        if (idx == -1) 99 else idx
                    }
                    
                    items(sortedTypes) { type ->
                        val foodItems = meals[type] ?: emptyList()
                        foodItems.forEach { item ->
                            MealBreakdownCard(type, item)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                
                // NUTRIENT BREAKDOWN
                item {
                    Text("Nutrient Breakdown", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            val totals = data.nutritionTotals ?: NutritionTotals()
                            val percentages = data.nutrientPercentages ?: emptyMap()
                            
                            NutrientRow("Protein", "${totals.protein.toInt()}g", percentages["protein_percent"] ?: 0, Color(0xFF4285F4))
                            Spacer(modifier = Modifier.height(16.dp))
                            NutrientRow("Carbs", "${totals.carbs.toInt()}g", percentages["carbs_percent"] ?: 0, Color(0xFFFF9800))
                            Spacer(modifier = Modifier.height(16.dp))
                            NutrientRow("Fats", "${totals.fat.toInt()}g", percentages["fat_percent"] ?: 0, Color(0xFF4CAF50))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
                item { Text("AI Coach suggestions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                        border = BorderStroke(1.dp, Color(0xFFDCEDC8))
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DarkGreen)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Coach Tip", fontWeight = FontWeight.Bold, color = DarkGreen)
                                Text(
                                    text = if (data.aiSuggestion.isNullOrBlank()) "Log your meals to get personalized nutrition advice from your AI Coach!" else data.aiSuggestion,
                                    fontSize = 14.sp,
                                    color = DarkGreen.copy(alpha = 0.8f),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun MealBreakdownCard(type: String, item: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(type.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                Text(item.food, color = Color.Gray, fontSize = 13.sp)
            }
            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                Text(
                    text = "${item.calories.toInt()} kcal",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun NutrientRow(name: String, value: String, percentage: Int, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
    }
}
