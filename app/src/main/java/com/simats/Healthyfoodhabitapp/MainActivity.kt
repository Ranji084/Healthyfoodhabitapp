package com.simats.Healthyfoodhabitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simats.Healthyfoodhabitapp.ui.screens.*
import com.simats.Healthyfoodhabitapp.ui.theme.HealthyfoodhabitappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthyfoodhabitappTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onProceedClick = {
                navController.navigate("welcome")
            })
        }
        composable("welcome") {
            WelcomeScreen(onAuthSuccess = {
                navController.navigate("profile")
            })
        }
        composable("profile") {
            ProfileScreen(onNextClick = {
                navController.navigate("goal")
            })
        }
        composable("goal") {
            GoalScreen(onStartClick = {
                navController.navigate("home")
            })
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("wellness") {
            WellnessScreen(navController)
        }
        composable(
            route = "add_meal?mealType={mealType}",
            arguments = listOf(navArgument("mealType") { 
                defaultValue = "Breakfast"
                type = NavType.StringType 
            })
        ) { backStackEntry ->
            val mealType = backStackEntry.arguments?.getString("mealType") ?: "Breakfast"
            AddMealScreen(navController, initialMealType = mealType)
        }
        composable("meal_result") {
            MealResultScreen(navController)
        }
        composable("report") {
            ReportScreen(navController)
        }
        composable("health_report") {
            HealthReportScreen(navController)
        }
        composable("progress") {
            ProgressScreen(navController)
        }
        composable("saved_report") {
            SavedReportScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("privacy_policy") {
            PrivacyPolicyScreen(navController)
        }
        composable("faqs") {
            FaqScreen(navController)
        }
        // Placeholder routes for navigation
        composable("water") { Box(modifier = Modifier.fillMaxSize()) { Text("Water Tracker") } }
        composable("stats") { Box(modifier = Modifier.fillMaxSize()) { Text("Statistics") } }
    }
}
