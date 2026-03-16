package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun MealResultScreen(
    navController: NavController,
    calories: String,
    protein: String,
    carbs: String,
    fat: String,
    advice: String,
    bmi: String,
    category: String,
    food: String,
    mealType: String
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    var loggedMeals by remember { mutableStateOf<List<MealResponse>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (userId != -1) {
            val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
            api.getTodayMeals(userId).enqueue(object : Callback<List<MealResponse>> {
                override fun onResponse(call: Call<List<MealResponse>>, response: Response<List<MealResponse>>) {
                    if (response.isSuccessful) {
                        loggedMeals = response.body() ?: emptyList()
                    }
                }
                override fun onFailure(call: Call<List<MealResponse>>, t: Throwable) {}
            })
        }
    }

    val nextMeal = navController.currentBackStackEntry
        ?.arguments?.getString("nextMeal") ?: "Breakfast"

    val buttonText = when (nextMeal.lowercase()) {
        "lunch" -> "Save & Add Lunch"
        "snack" -> "Save & Add Snacks"
        "dinner" -> "Save & Add Dinner"
        else -> "View Insights"
    }

    val nextRoute = when (nextMeal.lowercase()) {
        "lunch" -> "add_meal?mealType=Lunch"
        "snack" -> "add_meal?mealType=Snack"
        "dinner" -> "add_meal?mealType=Dinner"
        else -> "progress"
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = DarkGreen)
                    }
                    Text(
                        text = "Meal Analysis",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // To balance the back button
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "You ate: $food", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "$calories kcal", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Total Energy", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    NutrientSmallCard(label = "Protein", value = "${protein}g", modifier = Modifier.weight(1f), valueColor = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.width(12.dp))
                    NutrientSmallCard(label = "Carbs", value = "${carbs}g", modifier = Modifier.weight(1f), valueColor = Color(0xFF2196F3))
                    Spacer(modifier = Modifier.width(12.dp))
                    NutrientSmallCard(label = "Fat", value = "${fat}g", modifier = Modifier.weight(1f), valueColor = Color(0xFFFFC107))
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    border = BorderStroke(1.dp, Color(0xFFDCEDC8))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = DarkGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "AI Nutrition Advice", fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Removed the "Don't skip meal" warnings as requested
                        Text(
                            text = advice, 
                            color = DarkGreen.copy(alpha = 0.8f), 
                            fontSize = 14.sp, 
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Action Buttons
            item {
                Button(
                    onClick = {
                        navController.navigate(nextRoute)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedButton(
                    onClick = { navController.navigate("dashboard") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    border = BorderStroke(2.dp, DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Back to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
