package com.simats.Healthyfoodhabitapp.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun AddMealScreen(navController: NavController, initialMealType: String = "Breakfast") {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var selectedMealType by remember { mutableStateOf(initialMealType) }
    var mealInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showRecognitionError by remember { mutableStateOf(false) }

    val mealTypes = listOf(
        MealTypeData("Breakfast", "🍳"),
        MealTypeData("Lunch", "🥗"),
        MealTypeData("Snack", "🍎"),
        MealTypeData("Dinner", "🍛")
    )

    fun getNextMeal(current: String): String {
        return when (current.lowercase()) {
            "breakfast" -> "Lunch"
            "lunch" -> "Snack"
            "snack" -> "Dinner"
            else -> "Breakfast"
        }
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = DarkGreen
                        )
                    }

                    Text(
                        text = "Add Daily Meal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    items(mealTypes) { meal ->

                        val isSelected = selectedMealType.equals(meal.name, true)

                        Card(
                            modifier = Modifier
                                .width(90.dp)
                                .height(100.dp)
                                .clickable { selectedMealType = meal.name },

                            shape = RoundedCornerShape(20.dp),

                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (isSelected) DarkGreen else Color(0xFFF5F5F5)
                            )
                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(meal.emoji, fontSize = 24.sp)

                                Text(
                                    meal.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color =
                                        if (isSelected) Color.White else DarkGreen
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {

                OutlinedTextField(
                    value = mealInput,
                    onValueChange = { mealInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),

                    placeholder = {
                        Text("e.g. rice, chicken, apple")
                    },

                    shape = RoundedCornerShape(16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {

                Button(
                    onClick = {

                        if (mealInput.isBlank()) return@Button

                        val userId = sessionManager.getUserId()
                        val mealType = selectedMealType.lowercase()
                        val foodText = mealInput.trim()

                        isLoading = true

                        val api = RetrofitClient
                            .getInstance(context)
                            .create(ApiService::class.java)

                        val request = MealRequest(
                            userId = userId,
                            mealType = mealType,
                            foodText = foodText
                        )

                        api.addMeal(request).enqueue(object : Callback<MealResponse> {

                            override fun onResponse(
                                call: Call<MealResponse>,
                                response: Response<MealResponse>
                            ) {

                                isLoading = false

                                if (response.isSuccessful && response.body() != null) {

                                    val res = response.body()!!

                                    val nutrition = res.nutrition ?: Nutrition(
                                        res.calories ?: 0f,
                                        res.protein ?: 0f,
                                        res.carbs ?: 0f,
                                        res.fat ?: 0f
                                    )

                                    // Use ai_tip from server as advice
                                    val adv = Uri.encode(res.aiTip ?: res.nutritionAdvice ?: "")
                                    val cat = Uri.encode(res.bmiCategory ?: "")
                                    val foodEnc = Uri.encode(foodText)

                                    val nextMeal = getNextMeal(selectedMealType)

                                    navController.navigate(
                                        "meal_result?calories=${nutrition.calories}&protein=${nutrition.protein}&carbs=${nutrition.carbs}&fat=${nutrition.fat}&advice=$adv&bmi=${res.bmi ?: 0f}&category=$cat&food=$foodEnc&type=$mealType&nextMeal=$nextMeal"
                                    )

                                } else {

                                    Toast.makeText(
                                        context,
                                        "Server Error ${response.code()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(
                                call: Call<MealResponse>,
                                t: Throwable
                            ) {

                                isLoading = false

                                Toast.makeText(
                                    context,
                                    "Connection Failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(28.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen
                    ),

                    enabled = !isLoading
                ) {

                    if (isLoading)
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )

                    else
                        Text(
                            "Calculate Nutrients",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                }
            }
        }
    }
}