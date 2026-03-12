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
    
    var insightsData by remember { mutableStateOf<InsightsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
        val userId = sessionManager.getUserId()

        val request = InsightsRequest(userId)
        api.getViewInsights(request).enqueue(object : Callback<InsightsResponse> {
            override fun onResponse(call: Call<InsightsResponse>, response: Response<InsightsResponse>) {
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    insightsData = data
                    
                    // Direct sync to Home Page score
                    sessionManager.saveHealthScore(data.healthPercentage)
                    sessionManager.saveAiAdvice(data.aiSuggestion ?: "Keep logging!")
                }
            }

            override fun onFailure(call: Call<InsightsResponse>, t: Throwable) {
                isLoading = false
            }
        })
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF3F0E7)
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkGreen)
            }
        } else if (insightsData == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Could not load insights. Please check your connection.")
            }
        } else {
            val data = insightsData!!
            val nutrition = data.nutritionTotals
            
            // Fixed sequence: breakfast, lunch, snacks, dinner
            val order = listOf("breakfast", "lunch", "snacks", "snack", "dinner")
            val flattenedMeals = data.mealBreakdown?.flatMap { entry ->
                entry.value.map { foodItem ->
                    MealSummaryV2(entry.key, foodItem.food, 70) // Mapping Map to List for display
                }
            }?.sortedBy { meal ->
                val idx = order.indexOf(meal.mealType.lowercase())
                if (idx == -1) 99 else idx
            } ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Text("Your Progress 📈", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                
                // TODAY'S HEALTH (Directly reflects on Home)
                item {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
                        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Today's Health", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                Text("${data.healthPercentage}%", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reflected on Dashboard", color = Color.White, fontSize = 12.sp)
                                }
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                                CircularProgressIndicator(progress = { data.healthPercentage / 100f }, modifier = Modifier.fillMaxSize(), color = Color.White, strokeWidth = 6.dp, trackColor = Color.White.copy(alpha = 0.2f), strokeCap = StrokeCap.Round)
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                item { Text("Today's Meal Breakdown", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                items(flattenedMeals) { meal ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(meal.mealType.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreen)
                                    Text(meal.foodItems, fontSize = 13.sp, color = Color.Gray)
                                }
                                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                                    Text("${meal.score}%", color = DarkGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                
                // NUTRIENT BREAKDOWN
                item {
                    val p = if (nutrition != null) (nutrition.protein / 60 * 100).toInt().coerceIn(0, 100) else 0
                    val c = if (nutrition != null) (nutrition.carbs / 300 * 100).toInt().coerceIn(0, 100) else 0
                    val f = if (nutrition != null) (nutrition.fat / 70 * 100).toInt().coerceIn(0, 100) else 0
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nutrient Breakdown", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen)
                            Spacer(modifier = Modifier.height(12.dp))
                            NutrientBar("Protein", p, Color(0xFF2196F3))
                            Spacer(modifier = Modifier.height(8.dp))
                            NutrientBar("Carbs", c, Color(0xFFFFC107))
                            Spacer(modifier = Modifier.height(8.dp))
                            NutrientBar("Fats", f, Color(0xFF4CAF50))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                item { Text("AI Coach Suggestions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreen) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                item {
                    val allFood = flattenedMeals.joinToString(" ") { it.foodItems.lowercase() }
                    val dynamicAdvice = when {
                        allFood.isEmpty() -> "Log your first meal to get personalized advice!"
                        allFood.contains("apple") || allFood.contains("banana") || allFood.contains("fruit") -> 
                            "Vitamins on point! Combine fruit with a protein source like nuts for sustained energy. 🍎"
                        allFood.contains("chicken") || allFood.contains("egg") || allFood.contains("protein") -> 
                            "Great protein intake! Add some fiber-rich greens like spinach to balance the meal. 🍗"
                        allFood.contains("rice") || allFood.contains("bread") || allFood.contains("pasta") -> 
                            "Good fuel! Try whole-grain versions for better digestion and stable sugar levels. 🌾"
                        else -> data.aiSuggestion ?: "Looking good! Keep tracking to reach your daily goal."
                    }

                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), border = BorderStroke(1.dp, Color(0xFFC8E6C9))) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DarkGreen, modifier = Modifier.padding(top = 2.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Personalized Advice", fontWeight = FontWeight.Bold, color = DarkGreen)
                                Text(dynamicAdvice, fontSize = 12.sp, color = DarkGreen.copy(alpha = 0.8f), lineHeight = 16.sp)
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
fun NutrientBar(name: String, percent: Int, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("$percent%", fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}
