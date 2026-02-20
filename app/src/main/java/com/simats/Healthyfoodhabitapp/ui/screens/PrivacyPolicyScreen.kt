package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_meal?mealType=Breakfast") },
                containerColor = Color.White,
                contentColor = DarkGreen,
                shape = CircleShape,
                modifier = Modifier.offset(y = 50.dp).size(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
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
                        text = "Privacy Policy 🔐",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f).padding(end = 45.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F0E8).copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = DarkGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Your privacy matters to us. Here's how we handle your data.",
                            fontSize = 14.sp,
                            color = DarkGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Sections
            item { PrivacySection("1. Information We Collect", "We collect personal information you provide during registration, including your name, email address, age, gender, height, and weight. We also collect meal logs, wellness data (water intake, sleep timings), and app usage data to personalize your experience.") }
            item { PrivacySection("2. How We Use Your Data", "Your data is used to provide personalized health scores, meal recommendations, wellness insights, and progress tracking. We analyze your nutrition and wellness patterns to offer tailored suggestions that help you achieve your health goals.") }
            item { PrivacySection("3. Data Storage & Security", "All your data is stored securely on your device. We use industry-standard encryption to protect your personal information. We do not sell, trade, or share your personal data with third parties for marketing purposes.") }
            item { PrivacySection("4. Your Rights", "You have the right to access, modify, or delete your personal data at any time through the app settings. You can export your progress data using the save feature. If you delete your account, all associated data will be permanently removed.") }
            item { PrivacySection("5. Cookies & Analytics", "We may use anonymous analytics to improve app performance and user experience. No personally identifiable information is shared with analytics providers. You can opt out of analytics collection through the app settings.") }
            item { PrivacySection("6. Changes to This Policy", "We may update this privacy policy from time to time. Any changes will be reflected in the app, and we will notify you of significant updates. Continued use of the app after changes constitutes acceptance of the updated policy.") }
            item { PrivacySection("7. Contact Us", "If you have any questions or concerns about this privacy policy or your data, please contact us at support@healthyfoodhabit.com. We are committed to resolving any privacy-related issues promptly.") }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Last updated: February 2026",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}
