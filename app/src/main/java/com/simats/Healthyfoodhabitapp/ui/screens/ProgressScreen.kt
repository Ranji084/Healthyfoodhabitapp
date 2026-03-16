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

// Local helper to handle meal display data
data class MealDisplayItem(
    val type: String,
    val food: String,
    val kcal: Int
)

@Composable
fun ProgressScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    var insightsData by remember { mutableStateOf<InsightsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
        val userId = sessionManager.getUserId()
        Log.d("ProgressScreen", "Fetching insights for userId: $userId")

        val request = InsightsRequest(userId)
        api.getViewInsights(request).enqueue(object : Callback<InsightsResponse> {
            override fun onResponse(call: Call<InsightsResponse>, response: Response<InsightsResponse>) {
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    Log.d("ProgressScreen", "Success: ${data.healthPercentage}%")
                    insightsData = data
                    
                    // Cap the score at 98% for believability before saving
                    val believableScore = if (data.healthPercentage >= 100) 98 else data.healthPercentage
                    
                    // Direct sync to Home Page score
                    sessionManager.saveHealthScore(believableScore)
                    sessionManager.saveAiAdvice(data.aiSuggestion ?: "Keep logging!")
                } else {
                    Log.e("ProgressScreen", "Server Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<InsightsResponse>, t: Throwable) {
                isLoading = false
                Log.e("ProgressScreen", "Network Failure: ${t.message}", t)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Could not load insights.")
                    Text("Check Logcat for 'ProgressScreen' tag", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { 
                        isLoading = true
                        navController.navigate("progress") { popUpTo("progress") { inclusive = true } }
                    }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            val data = insightsData!!
            val percentages = data.nutrientPercentages
            
            // Fixed sequence: breakfast, lunch, snack, dinner
            val order = listOf("breakfast", "lunch", "snacks", "snack", "dinner")
            val flattenedMeals = data.mealBreakdown?.flatMap { entry ->
                entry.value.map { foodItem ->
                    // Map to our local display item with actual kcal
                    MealDisplayItem(entry.key, foodItem.food, foodItem.calories.toInt()) 
                }
            }?.sortedBy { meal ->
                val idx = order.indexOf(meal.type.lowercase())
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
                
                // TODAY'S HEALTH
                item {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
                        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Today's Health", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                // Displaying health percentage (capped at 98% for believability)
                                val displayScore = if (data.healthPercentage >= 100) 98 else data.healthPercentage
                                Text("$displayScore%", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
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
                                    Text(meal.type.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreen)
                                    Text(meal.food, fontSize = 13.sp, color = Color.Gray)
                                }
                                // Changed from Percentage to kcal as requested
                                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                                    Text("${meal.kcal} kcal", color = DarkGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                
                // NUTRIENT BREAKDOWN
                item {
                    val p = percentages?.get("protein_percent") ?: 0
                    val c = percentages?.get("carbs_percent") ?: 0
                    val f = percentages?.get("fat_percent") ?: 0
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
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), border = BorderStroke(1.dp, Color(0xFFC8E6C9))) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DarkGreen, modifier = Modifier.padding(top = 2.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Personalized Advice", fontWeight = FontWeight.Bold, color = DarkGreen)
                                Text(data.aiSuggestion ?: "Keep tracking to reach your daily goal.", fontSize = 12.sp, color = DarkGreen.copy(alpha = 0.8f), lineHeight = 16.sp)
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
