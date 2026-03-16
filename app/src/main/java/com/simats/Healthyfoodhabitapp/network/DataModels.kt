package com.simats.Healthyfoodhabitapp.network

import com.google.gson.annotations.SerializedName

// -------- USER & AUTH --------

data class User(
    @SerializedName("user_id") val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password: String? = null,
    val age: Int? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val gender: String? = null,
    val goal: String? = null
)

data class AuthResponse(
    val status: String? = null,
    val message: String? = null,
    val name: String? = null,
    @SerializedName("user_id") val userId: Int? = null,
    val id: Int? = null,
    val user: User? = null
)

// -------- FORGOT PASSWORD --------

data class ForgotPasswordRequest(
    val email: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ResetPasswordRequest(
    val email: String,
    @SerializedName("password") val newPassword: String
)

// -------- MEAL --------

data class MealRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("food_text") val foodText: String
)

data class MealResponse(
    val status: String? = null,
    val nutrition: Nutrition? = null,
    @SerializedName("food_items") val foodItems: String? = null,
    @SerializedName("meal_type") val mealType: String? = null,
    val calories: Float? = null,
    val protein: Float? = null,
    val carbs: Float? = null,
    val fat: Float? = null,
    @SerializedName("nutrition_advice") val nutritionAdvice: String? = null,
    @SerializedName("ai_tip") val aiTip: String? = null,
    val bmi: Float? = null,
    @SerializedName("bmi_category") val bmiCategory: String? = null
)

data class Nutrition(
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float
)

// -------- INSIGHTS REQUEST/RESPONSE --------

data class InsightsRequest(
    @SerializedName("user_id") val userId: Int
)

data class InsightsResponse(
    @SerializedName("health_percentage") val healthPercentage: Int = 0,
    @SerializedName("nutrition_totals") val nutritionTotals: NutritionTotals? = null,
    @SerializedName("nutrient_percentages") val nutrientPercentages: Map<String, Int>? = null,
    @SerializedName("meal_breakdown") val mealBreakdown: Map<String, List<FoodItem>>? = null,
    @SerializedName("ai_suggestion") val aiSuggestion: String? = ""
)
data class MealItem(
    val food: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)

data class NutritionTotals(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

data class FoodItem(
    val food: String = "",
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

data class MealSummaryV2(
    @SerializedName("meal_type") val mealType: String = "",
    @SerializedName("food_items") val foodItems: String = "",
    val score: Int = 0
)

// UI Logic helper classes
data class TodayHealth(
    val score: Int,
    val change: Int,
    val changeText: String,
    val circularProgress: Float
)

data class AiSuggestion(
    val title: String,
    val description: String
)

// -------- PROGRESS RESPONSE --------

data class ProgressResponse(
    val healthScore: Int = 0,
    val totalProtein: Double = 0.0,
    val totalCarbs: Double = 0.0,
    val totalFats: Double = 0.0,
    val aiAdvice: String = "",
    val meals: List<MealSummary> = emptyList()
)

data class MealSummary(
    val mealType: String,
    val foodItems: String,
    val score: Int
)

// -------- OTHER --------

data class RecommendationResponse(
    val status: String? = null,
    val recommendation: String? = null,
    @SerializedName("total_calories") val totalCalories: Float? = 0f,
    @SerializedName("total_protein") val totalProtein: Float? = 0f,
    @SerializedName("total_carbs") val totalCarbs: Float? = 0f,
    @SerializedName("total_fats") val totalFats: Float? = 0f
)

data class SelectedDateRequest(val email: String, val date: String)
data class CommonResponse(val status: String, val message: String)
data class Review(val email: String, val rating: Int, val feedback: String? = "")
