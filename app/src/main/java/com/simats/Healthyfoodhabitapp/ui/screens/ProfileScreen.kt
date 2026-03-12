package com.simats.Healthyfoodhabitapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.SessionManager
import com.simats.Healthyfoodhabitapp.network.*
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var name by remember { mutableStateOf(sessionManager.getUserName()) }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val genders = listOf("Male", "Female", "Other")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold, color = DarkGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {

            Text(
                text = "Update your stats 📝",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // NAME
                    Text("Name", fontWeight = FontWeight.Bold, color = DarkGreen)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // AGE + GENDER
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Age", fontWeight = FontWeight.Bold, color = DarkGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gender", fontWeight = FontWeight.Bold, color = DarkGreen)
                            Spacer(modifier = Modifier.height(6.dp))

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = gender,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(Icons.Default.ArrowDropDown, null)
                                    },
                                    modifier = Modifier.menuAnchor(),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    genders.forEach {
                                        DropdownMenuItem(
                                            text = { Text(it) },
                                            onClick = {
                                                gender = it
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // HEIGHT + WEIGHT
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Height (cm)", fontWeight = FontWeight.Bold, color = DarkGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = { Icon(Icons.Default.Straighten, null) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Weight (kg)", fontWeight = FontWeight.Bold, color = DarkGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = { Icon(Icons.Default.MonitorWeight, null) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // SAVE BUTTON
            Button(
                onClick = {

                    val email = sessionManager.getUserEmail()
                    val userId = sessionManager.getUserId()
                    
                    if (name.isBlank() || email.isNullOrBlank() || userId == -1) {
                        Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    val userUpdate = User(
                        id = userId,
                        name = name,
                        email = email,
                        password = null, // Send null to avoid changing password or causing errors
                        age = age.toIntOrNull(),
                        height = height.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        gender = if(gender.isNotBlank()) gender else null
                    )

                    RetrofitClient.getInstance(context)
                        .create(ApiService::class.java)
                        .updateProfile(userUpdate)
                        .enqueue(object : Callback<AuthResponse> {

                            override fun onResponse(
                                call: Call<AuthResponse>,
                                response: Response<AuthResponse>
                            ) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    sessionManager.saveLoginSession(email, name, userId)
                                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    Log.e("ProfileUpdate", "Error: ${response.code()} ${response.errorBody()?.string()}")
                                    Toast.makeText(context, "Update Failed. Try again.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = !isLoading
            ) {
                if (isLoading)
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else
                    Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }
}
