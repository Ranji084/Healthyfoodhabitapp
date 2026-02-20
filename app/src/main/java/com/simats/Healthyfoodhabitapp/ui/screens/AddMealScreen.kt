package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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

@Composable
fun AddMealScreen(navController: NavController, initialMealType: String = "Breakfast") {
    var selectedMealType by remember { mutableStateOf(initialMealType) }
    var mealInput by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }

    // Sync selectedMealType with initialMealType when it changes via navigation
    LaunchedEffect(initialMealType) {
        selectedMealType = initialMealType
    }

    val mealTypes = listOf(
        MealTypeData("Breakfast", "🍳"),
        MealTypeData("Lunch", "🥗"),
        MealTypeData("Dinner", "🍛"),
        MealTypeData("Snack", "🍎")
    )

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
                        text = "Add Meal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f).padding(end = 45.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Meal Type Selector
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mealTypes) { meal ->
                        val isSelected = selectedMealType == meal.name
                        Card(
                            modifier = Modifier
                                .width(90.dp)
                                .height(100.dp)
                                .clickable { 
                                    selectedMealType = meal.name
                                    showResults = false
                                    mealInput = ""
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DarkGreen else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = meal.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = meal.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else DarkGreen
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            // Title
            item {
                val currentEmoji = mealTypes.find { it.name == selectedMealType }?.emoji ?: "🍳"
                Text(
                    text = "What did you eat for\n$selectedMealType ? $currentEmoji",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Be specific! Try \"2 eggs, 1 toast, black coffee\"",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Input Display Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFF0F2F5))
                ) {
                    TextField(
                        value = mealInput,
                        onValueChange = { if(!showResults) mealInput = it },
                        modifier = Modifier.fillMaxSize(),
                        placeholder = { Text("Enter your meal details...", color = Color.LightGray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 18.sp, color = DarkGreen),
                        readOnly = showResults
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Action Button
            item {
                if (!showResults) {
                    Button(
                        onClick = { if(mealInput.isNotBlank()) showResults = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(text = "Calculate Nutrients 🧱", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showResults) {
                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Score Card (Centered Content)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF00897B))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "$selectedMealType Score", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "60%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Based on nutrient balance", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Nutrient Grid
                item {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            NutrientSmallCard(label = "Minerals", value = "62%", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(16.dp))
                            NutrientSmallCard(label = "Protein", value = "76%", modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            NutrientSmallCard(label = "Calories", value = "305", modifier = Modifier.weight(1f), valueColor = Color(0xFFFFB74D))
                            Spacer(modifier = Modifier.width(16.dp))
                            NutrientSmallCard(label = "Fiber & Carbs", value = "42%", modifier = Modifier.weight(1f))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // Final Action Buttons
                item {
                    OutlinedButton(
                        onClick = { 
                            mealInput = ""
                            showResults = false
                            selectedMealType = when(selectedMealType) {
                                "Breakfast" -> "Lunch"
                                "Lunch" -> "Dinner"
                                "Dinner" -> "Snack"
                                else -> "Breakfast"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0F0E8))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Save & Add Next Meal", color = DarkGreen, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate("progress") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(text = "View Day Insights 📊", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
