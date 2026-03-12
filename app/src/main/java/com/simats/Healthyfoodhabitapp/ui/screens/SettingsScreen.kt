package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun SettingsScreen(navController: NavController) {
    var rating by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onLogout = {
                showLogoutDialog = false
                navController.navigate("welcome") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_meal") },
                containerColor = Color.White,
                contentColor = DarkGreen,
                shape = CircleShape,
                modifier = Modifier.offset(y = 50.dp).size(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = { navController.popBackStack() },
                        shape = CircleShape,
                        color = Color(0xFFF0F2F5).copy(alpha = 0.5f),
                        modifier = Modifier.size(45.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = DarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = "Settings ⚙️",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f).padding(end = 45.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Profile Section
            item {
                SectionHeader("Profile")
                Spacer(modifier = Modifier.height(12.dp))
                SettingsItem(
                    icon = Icons.Default.Person,
                    iconBgColor = Color(0xFFE0F0E8),
                    title = "Edit Profile",
                    subtitle = "Update your stats & goals",
                    onClick = { navController.navigate("profile") }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Rate Us Section
            item {
                SectionHeader("Rate Us")
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "⭐", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Enjoying the app?", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
                        Text(text = "Tap a star to rate your experience", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..5).forEach { index ->
                                Icon(
                                    imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = if (index <= rating) Color(0xFFFFD700) else Color.LightGray,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable { rating = index }
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // About Section
            item {
                SectionHeader("About")
                Spacer(modifier = Modifier.height(12.dp))
                SettingsItem(
                    icon = Icons.Outlined.Info,
                    iconBgColor = Color(0xFFC1F0D1).copy(alpha = 0.5f),
                    title = "About App",
                    subtitle = "Learn more about Healthy Habit",
                    onClick = { navController.navigate("introduction") }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Support Section
            item {
                SectionHeader("Support")
                Spacer(modifier = Modifier.height(12.dp))
                SettingsItem(
                    icon = Icons.Outlined.Shield,
                    iconBgColor = Color(0xFFE3F2FD),
                    iconTintColor = Color(0xFF2196F3),
                    title = "Privacy Policy",
                    subtitle = "How we handle your data",
                    onClick = { navController.navigate("privacy_policy") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsItem(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    iconBgColor = Color(0xFFF3E5F5),
                    iconTintColor = Color(0xFF9C27B0),
                    title = "FAQs",
                    subtitle = "Frequently asked questions",
                    onClick = { navController.navigate("faqs") }
                )
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            // Log Out
            item {
                Row(
                    modifier = Modifier.clickable { showLogoutDialog = true }.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Log Out", tint = Color(0xFFE57373))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Log Out", color = Color(0xFFE57373), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(onDismiss: () -> Unit, onLogout: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.ReportProblem,
                            contentDescription = null,
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Log Out?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Are you sure you want to log out?\nYou'll need to sign in again.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text("Cancel", color = DarkGreen, fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                    ) {
                        Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
