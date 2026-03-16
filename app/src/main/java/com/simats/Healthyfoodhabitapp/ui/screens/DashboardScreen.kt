package com.simats.Healthyfoodhabitapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.network.*
import com.simats.Healthyfoodhabitapp.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DashboardScreen(navController: NavController) {

    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    val userName = sessionManager.getUserName()

    var todayMeals by remember { mutableStateOf<List<MealResponse>>(emptyList()) }
    var healthScore by remember { mutableStateOf(sessionManager.getHealthScore()) }

    val api = RetrofitClient
        .getInstance(context)
        .create(ApiService::class.java)

    /*
    Load today's meals
     */
    LaunchedEffect(Unit) {

        api.getTodayMeals(sessionManager.getUserId())
            .enqueue(object : Callback<List<MealResponse>> {

                override fun onResponse(
                    call: Call<List<MealResponse>>,
                    response: Response<List<MealResponse>>
                ) {
                    if (response.isSuccessful) {

                        todayMeals = response.body() ?: emptyList()

                        Log.d("DashboardMeals", todayMeals.toString())
                    }
                }

                override fun onFailure(call: Call<List<MealResponse>>, t: Throwable) {

                    Log.e("DashboardMeals", t.message.toString())
                }
            })


        /*
        Load health score from Progress API
         */
        api.getDailyProgress(sessionManager.getUserId())
            .enqueue(object : Callback<ProgressResponse> {

                override fun onResponse(
                    call: Call<ProgressResponse>,
                    response: Response<ProgressResponse>
                ) {

                    if (response.isSuccessful) {
                        val score = response.body()?.healthScore ?: 0
                        healthScore = score
                        sessionManager.saveHealthScore(score)
                    }
                }

                override fun onFailure(call: Call<ProgressResponse>, t: Throwable) {

                    Log.e("HealthScore", t.message.toString())
                }
            })

    }

    /*
    UI START
     */

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            item {

                Text(
                    text = "Hello $userName",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Daily Health Score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$healthScore %",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Today's Meals",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            /*
            Show each meal one after another
             */

            items(todayMeals) { meal ->

                MealCard(meal)

            }

            item {

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        navController.navigate("add_meal?mealType=Breakfast")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Add Today's Meal")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate("progress")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("View Insights")
                }
            }

        }
    }
}

@Composable
fun MealCard(meal: MealResponse) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                text = (meal.mealType ?: "Meal").uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Food: ${meal.foodItems ?: "N/A"}")

            Text("Calories: ${meal.calories ?: 0f}")

            Text("Protein: ${meal.protein ?: 0f} g")

            Text("Carbs: ${meal.carbs ?: 0f} g")

            Text("Fat: ${meal.fat ?: 0f} g")

        }

    }

}
