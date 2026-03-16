package com.simats.Healthyfoodhabitapp.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(@Body user: User): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: User): Call<AuthResponse>

    @POST("forgot_password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<AuthResponse>

    @POST("verify_otp")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<AuthResponse>

    @POST("reset_password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<AuthResponse>

    @POST("add_meal")
    fun addMeal(@Body request: MealRequest): Call<MealResponse>

    @POST("view_insights")
    fun getViewInsights(@Body request: InsightsRequest): Call<InsightsResponse>

    @GET("progress/{userId}")
    fun getDailyProgress(@Path("userId") userId: Int): Call<ProgressResponse>

    @GET("meals/today/{userId}")
    fun getTodayMeals(@Path("userId") userId: Int): Call<List<MealResponse>>

    @GET("recommend/{user_id}")
    fun getRecommendation(@Path("user_id") userId: Int): Call<RecommendationResponse>

    @POST("update_profile")
    fun updateProfile(@Body user: User): Call<AuthResponse>

    @POST("add_review")
    fun addReview(@Body review: Review): Call<AuthResponse>

    @POST("save_date")
    fun saveDate(@Body selectedDate: SelectedDateRequest): Call<AuthResponse>
}
