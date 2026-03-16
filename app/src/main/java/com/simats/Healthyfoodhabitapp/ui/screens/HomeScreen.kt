package com.simats.Healthyfoodhabitapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    // Use state to track the name so it updates when session manager changes
    var userName by remember { mutableStateOf(sessionManager.getUserName()) }
    val userId = sessionManager.getUserId()
    
    var healthScore by remember { mutableIntStateOf(sessionManager.getHealthScore()) }
    var protein by remember { mutableStateOf("0g") }
    var carbs by remember { mutableStateOf("0g") }
    var fats by remember { mutableStateOf("0g") }

    var isLoadingData by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Refresh name whenever we enter home
        userName = sessionManager.getUserName()

        if (userId != -1) {
            val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
            
            val request = InsightsRequest(userId)
            api.getViewInsights(request).enqueue(object : Callback<InsightsResponse> {
                override fun onResponse(call: Call<InsightsResponse>, response: Response<InsightsResponse>) {
                    isLoadingData = false
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        
                        // Cap the score at 98% for believability to match ProgressScreen
                        val believableScore = if (data.healthPercentage >= 100) 98 else data.healthPercentage
                        healthScore = believableScore
                        sessionManager.saveHealthScore(healthScore)
                        
                        val nutrition = data.nutritionTotals
                        protein = "${nutrition?.protein?.toInt() ?: 0}g"
                        carbs = "${nutrition?.carbs?.toInt() ?: 0}g"
                        fats = "${nutrition?.fat?.toInt() ?: 0}g"
                    }
                }
                override fun onFailure(call: Call<InsightsResponse>, t: Throwable) {
                    isLoadingData = false
                }
            })
        } else {
            isLoadingData = false
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
        if (isLoadingData) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hi, $userName ! 👋",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier.size(48.dp).clickable { navController.navigate("profile") }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = "Profile", tint = DarkGreen, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

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
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { healthScore / 100f },
                                    modifier = Modifier.size(140.dp),
                                    color = Color(0xFF00C853),
                                    strokeWidth = 12.dp,
                                    trackColor = Color.White.copy(alpha = 0.1f),
                                    strokeCap = StrokeCap.Round
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "$healthScore%", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Health Score", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                StatItem(protein, "Protein")
                                StatItem(carbs, "Carbs")
                                StatItem(fats, "Fats")
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "BMI Calculator", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            var heightInput by remember { mutableStateOf("") }
                            var weightInput by remember { mutableStateOf("") }

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = heightInput,
                                    onValueChange = { heightInput = it },
                                    label = { Text("Height (cm)") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                OutlinedTextField(
                                    value = weightInput,
                                    onValueChange = { weightInput = it },
                                    label = { Text("Weight (kg)") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = {
                                    val h = heightInput.toFloatOrNull()
                                    val w = weightInput.toFloatOrNull()
                                    if (h != null && w != null && h > 0) {
                                        val bmiValue = w / (h / 100).pow(2)
                                        val category = when {
                                            bmiValue < 18.5 -> "Underweight"
                                            bmiValue < 25 -> "Normal weight"
                                            bmiValue < 30 -> "Overweight"
                                            else -> "Obese"
                                        }
                                        val bmiStr = String.format(Locale.getDefault(), "%.1f", bmiValue)
                                        navController.navigate("bmi_result?bmi=$bmiStr&category=$category")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Calculate BMI", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }

                item {
                    Button(
                        onClick = { navController.navigate("add_meal?mealType=Breakfast") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Add Today's Meal", fontWeight = FontWeight.Bold)
                    }
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    OutlinedButton(
                        onClick = { navController.navigate("progress") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, DarkGreen)
                    ) {
                        Text("View Insights", fontWeight = FontWeight.Bold, color = DarkGreen)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
