package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun WelcomeScreen(onAuthSuccess: () -> Unit) {
    var isRegisterSelected by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Waving Hand Emoji
        Text(text = "👋", fontSize = 64.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isRegisterSelected) 
                "Create an account to start your healthy\njourney." 
            else 
                "Sign in to continue your healthy journey.",
            fontSize = 16.sp,
            color = DarkGreen.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Register/Login Toggle
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = Color(0xFFF0F2F5),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Register Tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clickable { isRegisterSelected = true },
                    color = if (isRegisterSelected) DarkGreen else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = if (isRegisterSelected) Color.White else DarkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Register",
                            color = if (isRegisterSelected) Color.White else DarkGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Login Tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clickable { isRegisterSelected = false },
                    color = if (!isRegisterSelected) DarkGreen else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = null,
                            tint = if (!isRegisterSelected) Color.White else DarkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Login",
                            color = if (!isRegisterSelected) Color.White else DarkGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Email Address", fontWeight = FontWeight.Bold, color = DarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("you@example.com", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFF0F2F5),
                    focusedBorderColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Password", fontWeight = FontWeight.Bold, color = DarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Min 6 characters", color = Color.LightGray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFF0F2F5),
                    focusedBorderColor = DarkGreen
                )
            )

            if (isRegisterSelected) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Confirm Password", fontWeight = FontWeight.Bold, color = DarkGreen)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Re-enter password", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFF0F2F5),
                        focusedBorderColor = DarkGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button (Sign In or Create Account)
        Button(
            onClick = onAuthSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF679B89), Color(0xFF5E9180))
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isRegisterSelected) "Create Account" else "Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Text
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isRegisterSelected) "Already have an account? " else "Don't have an account? ",
                color = DarkGreen.copy(alpha = 0.7f)
            )
            Text(
                text = if (isRegisterSelected) "Login" else "Register",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { isRegisterSelected = !isRegisterSelected }
            )
        }
    }
}
