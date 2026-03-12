package com.simats.Healthyfoodhabitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simats.Healthyfoodhabitapp.network.*
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
    val context = LocalContext.current
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onProceedClick = {
                navController.navigate("welcome") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("welcome") {
            WelcomeScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable(
            route = "otp_screen?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(navController, email)
        }
        composable(
            route = "reset_password_screen?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordScreen(navController, email)
        }
        composable("introduction") {
            IntroductionScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("dashboard") {
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
        composable(
            route = "meal_result?calories={calories}&protein={protein}&carbs={carbs}&fat={fat}&advice={advice}&bmi={bmi}&category={category}&food={food}&type={type}&nextMeal={nextMeal}",
            arguments = listOf(
                navArgument("calories") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("protein") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("carbs") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("fat") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("advice") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("bmi") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("category") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("food") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("type") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("nextMeal") { type = NavType.StringType; nullable = true; defaultValue = "Breakfast" }
            )
        ) { backStackEntry ->
            MealResultScreen(
                navController = navController,
                calories = backStackEntry.arguments?.getString("calories") ?: "0",
                protein = backStackEntry.arguments?.getString("protein") ?: "0",
                carbs = backStackEntry.arguments?.getString("carbs") ?: "0",
                fat = backStackEntry.arguments?.getString("fat") ?: "0",
                advice = backStackEntry.arguments?.getString("advice") ?: "",
                bmi = backStackEntry.arguments?.getString("bmi") ?: "0",
                category = backStackEntry.arguments?.getString("category") ?: "",
                food = backStackEntry.arguments?.getString("food") ?: "",
                mealType = backStackEntry.arguments?.getString("type") ?: ""
            )
        }
        composable(
            route = "bmi_result?bmi={bmi}&category={category}",
            arguments = listOf(
                navArgument("bmi") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("category") { type = NavType.StringType; nullable = true; defaultValue = "" }
            )
        ) { backStackEntry ->
            BmiResultScreen(
                navController = navController,
                bmi = backStackEntry.arguments?.getString("bmi") ?: "0",
                category = backStackEntry.arguments?.getString("category") ?: ""
            )
        }
        composable("report?water={water}&sleep={sleep}&bedTime={bedTime}&wakeTime={wakeTime}",
            arguments = listOf(
                navArgument("water") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("sleep") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("bedTime") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("wakeTime") { type = NavType.StringType; nullable = true; defaultValue = "" }
            )
        ) { backStackEntry ->
            val water = backStackEntry.arguments?.getString("water") ?: "0"
            val sleep = backStackEntry.arguments?.getString("sleep") ?: "0"
            val bedTime = backStackEntry.arguments?.getString("bedTime") ?: ""
            val wakeTime = backStackEntry.arguments?.getString("wakeTime") ?: ""
            ReportScreen(navController, water, sleep, bedTime, wakeTime)
        }
        composable("health_report") {
            HealthReportScreen(navController)
        }
        composable("progress") {
            ProgressScreen(navController)
        }
        composable(
            route = "saved_report?water={water}&sleep={sleep}&bedTime={bedTime}&wakeTime={wakeTime}&score={score}",
            arguments = listOf(
                navArgument("water") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("sleep") { type = NavType.StringType; nullable = true; defaultValue = "0" },
                navArgument("bedTime") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("wakeTime") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("score") { type = NavType.StringType; nullable = true; defaultValue = "0" }
            )
        ) { backStackEntry ->
            val water = backStackEntry.arguments?.getString("water") ?: "0"
            val sleep = backStackEntry.arguments?.getString("sleep") ?: "0"
            val bedTime = backStackEntry.arguments?.getString("bedTime") ?: ""
            val wakeTime = backStackEntry.arguments?.getString("wakeTime") ?: ""
            val score = backStackEntry.arguments?.getString("score") ?: "0"
            SavedReportScreen(navController, water, sleep, bedTime, wakeTime, score)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("privacy_policy") {
            PrivacyPolicyScreen(navController)
        }
        composable("faqs") {
            FaqScreen(navController)
        }
        composable("insights") { Box(modifier = Modifier.fillMaxSize()) { Text("Insights Screen") } }
        composable("water") { Box(modifier = Modifier.fillMaxSize()) { Text("Water Tracker") } }
        composable("stats") { Box(modifier = Modifier.fillMaxSize()) { Text("Statistics") } }
    }
}
