package com.simats.Healthyfoodhabitapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.network.*
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController, email: String) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color.White)
    )

    // Function to validate password strength
    fun isPasswordValid(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasLetter && hasDigit && hasSpecialChar
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password", fontWeight = FontWeight.Bold, color = DarkGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(gradient).padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create a new strong password", fontSize = 16.sp, color = DarkGreen)
                Spacer(modifier = Modifier.height(32.dp))

                InputField(label = "New Password", value = newPassword, onValueChange = { newPassword = it }, placeholder = "••••••••", isPassword = true)
                Spacer(modifier = Modifier.height(16.dp))
                InputField(label = "Confirm Password", value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "••••••••", isPassword = true)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        if (!isPasswordValid(newPassword)) {
                            Toast.makeText(context, "Password must contain alphabet, numbers and a special characters", Toast.LENGTH_LONG).show()
                            return@Button
                        }

                        if (newPassword != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isLoading = true
                        val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
                        
                        Log.d("ResetPassword", "Request for email: $email")
                        
                        api.resetPassword(ResetPasswordRequest(email, newPassword)).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    // Robust check for success status or message
                                    val isSuccess = body?.status == "success" || 
                                                 body?.message?.contains("success", ignoreCase = true) == true ||
                                                 body?.message?.contains("updated", ignoreCase = true) == true ||
                                                 body?.message?.contains("changed", ignoreCase = true) == true

                                    if (isSuccess) {
                                        Toast.makeText(context, "Password reset successfully!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("welcome") {
                                            popUpTo("welcome") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, body?.message ?: "Reset failed", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(20.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Reset Password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
