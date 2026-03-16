package com.simats.Healthyfoodhabitapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.Healthyfoodhabitapp.R
import com.simats.Healthyfoodhabitapp.SessionManager
import com.simats.Healthyfoodhabitapp.network.*
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun WelcomeScreen(onAuthSuccess: () -> Unit, onForgotPasswordClick: () -> Unit) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var isRegisterSelected by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Weight Loss") }

    var isLoading by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }
    var showIpDialog by remember { mutableStateOf(false) }

    var currentIp by remember {
        mutableStateOf(
            sessionManager.getBaseUrl()
                .removePrefix("http://")
                .removeSuffix(":5000/")
        )
    }

    val scrollState = rememberScrollState()

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF5F5F5), Color.White)
    )

    val api = RetrofitClient.getInstance(context).create(ApiService::class.java)

    // Helper for password validation
    fun isPasswordValid(p: String): Boolean {
        val hasLetter = p.any { it.isLetter() }
        val hasDigit = p.any { it.isDigit() }
        val hasSpecialChar = p.any { !it.isLetterOrDigit() }
        return p.length >= 8 && hasLetter && hasDigit && hasSpecialChar
    }

    if (showIpDialog) {
        AlertDialog(
            onDismissRequest = { showIpDialog = false },
            title = { Text("Server Settings") },
            text = {
                Column {
                    Text("Enter Computer IP (ipconfig):")
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = currentIp,
                        onValueChange = { currentIp = it },
                        placeholder = { Text("e.g. 10.199.42.201") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    sessionManager.saveBaseUrl(currentIp)
                    showIpDialog = false
                    Toast.makeText(context, "IP Updated!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Save")
                }
            }
        )
    }

    if (showSuccessPopup) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Success! 🎉", fontWeight = FontWeight.Bold, color = DarkGreen) },
            text = { Text("Redirecting...", fontSize = 16.sp) },
            shape = RoundedCornerShape(20.dp)
        )

        LaunchedEffect(Unit) {
            delay(1000)
            showSuccessPopup = false
            onAuthSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { showIpDialog = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = DarkGreen)
                }
            }

            // LOGOS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left_logo),
                    contentDescription = "Left Logo",
                    modifier = Modifier.size(115.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.right_logo),
                    contentDescription = "Right Logo",
                    modifier = Modifier.size(115.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Healthy Food Habit",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { isRegisterSelected = true },
                        color = if (isRegisterSelected) DarkGreen else Color.Transparent
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "Register",
                                color = if (isRegisterSelected) Color.White else DarkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { isRegisterSelected = false },
                        color = if (!isRegisterSelected) DarkGreen else Color.Transparent
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "Login",
                                color = if (!isRegisterSelected) Color.White else DarkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {

                InputField("Email Address", email, { email = it }, "you@example.com")

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    "Password",
                    password,
                    { password = it },
                    "Password",
                    isPassword = true
                )

                if (!isRegisterSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = DarkGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                onForgotPasswordClick()
                            }
                        )
                    }
                }

                if (isRegisterSelected) {

                    Spacer(modifier = Modifier.height(16.dp))

                    InputField(
                        "Confirm Password",
                        confirmPassword,
                        { confirmPassword = it },
                        "Confirm",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InputField(
                        "Full Name",
                        name,
                        { name = it },
                        "Name"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InputField(
                        "Age",
                        age,
                        { age = it },
                        "25",
                        KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Choose Your Goal",
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )

                    val goals = listOf("Weight Loss", "Maintain Weight", "Muscle Gain")

                    goals.forEach { g ->
                        val selected = goal == g
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { goal = g },
                            shape = RoundedCornerShape(16.dp),
                            color = if (selected) DarkGreen else Color.White,
                            border = BorderStroke(1.dp, DarkGreen)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = g,
                                    color = if (selected) Color.White else DarkGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (isRegisterSelected) {
                        if (!isPasswordValid(password)) {
                            Toast.makeText(context, "Password must be at least 8 characters and contain alphabet, numbers and a special characters", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    isLoading = true

                    val user = User(
                        name = if (isRegisterSelected) name else "",
                        email = email.trim(),
                        password = password.trim(),
                        age = age.toIntOrNull(),
                        goal = goal
                    )

                    val call: Call<AuthResponse> =
                        if (isRegisterSelected) api.register(user)
                        else api.login(user)

                    call.enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(
                            call: Call<AuthResponse>,
                            response: Response<AuthResponse>
                        ) {
                            isLoading = false
                            if (response.isSuccessful && response.body() != null) {
                                val res = response.body()!!
                                if (res.status == "success") {
                                    val capturedId = res.userId ?: res.id ?: res.user?.id ?: -1
                                    sessionManager.saveLoginSession(
                                        email.trim(),
                                        res.name ?: res.user?.name ?: name ?: "",
                                        capturedId
                                    )
                                    if (isRegisterSelected) showSuccessPopup = true
                                    else onAuthSuccess()
                                } else {
                                    Toast.makeText(context, res.message ?: "Error", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            isLoading = false
                            Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(20.dp),
                enabled = !isLoading
            ) {
                if (isLoading)
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else
                    Text(
                        if (isRegisterSelected) "Create Account" else "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        Text(
            text = "2026©powered by SIMATS Engineering",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            fontSize = 12.sp,
            color = Color.Gray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            trailingIcon = {
                if (isPassword) {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null, tint = DarkGreen)
                    }
                }
            },
            visualTransformation =
                if (isPassword && !passwordVisible)
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                else
                    androidx.compose.ui.text.input.VisualTransformation.None
        )
    }
}
