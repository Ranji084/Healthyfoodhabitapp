package com.simats.Healthyfoodhabitapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
fun OtpScreen(navController: NavController, email: String) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verify OTP", fontWeight = FontWeight.Bold, color = DarkGreen) },
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
                Text("Enter the 6-digit OTP sent to $email", fontSize = 16.sp, color = DarkGreen)
                Spacer(modifier = Modifier.height(32.dp))

                // Using internal InputField structure for OTP
                Column {
                    Text(
                        text = "OTP",
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("123456") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        if (otp.isEmpty()) {
                            Toast.makeText(context, "Please enter OTP", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isLoading = true
                        val api = RetrofitClient.getInstance(context).create(ApiService::class.java)
                        api.verifyOtp(VerifyOtpRequest(email, otp)).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    // Check for "success" status OR a message indicating OTP verified
                                    val isSuccess = body?.status == "success" || 
                                                 body?.message?.contains("verified", ignoreCase = true) == true
                                    
                                    if (isSuccess) {
                                        Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("reset_password_screen?email=$email")
                                    } else {
                                        Toast.makeText(context, body?.message ?: "Verification failed", Toast.LENGTH_SHORT).show()
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
                    else Text("Verify OTP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
