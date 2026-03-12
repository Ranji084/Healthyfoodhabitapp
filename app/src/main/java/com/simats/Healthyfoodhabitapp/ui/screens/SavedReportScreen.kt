package com.simats.Healthyfoodhabitapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.PdfHelper
import com.simats.Healthyfoodhabitapp.SessionManager
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun SavedReportScreen(
    navController: NavController,
    water: String,
    sleep: String,
    bedTime: String,
    wakeTime: String,
    score: String
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val username = sessionManager.getUserName()
    val email = sessionManager.getUserEmail() ?: ""

    // Automatically generate and save PDF when screen opens
    LaunchedEffect(Unit) {
        val isSaved = PdfHelper.generateAndSaveWellnessPdf(
            context = context,
            username = username,
            email = email, // FIXED: Passing the email parameter
            water = water,
            sleep = sleep,
            score = score
        )
        if (isSaved) {
            Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Failed to save PDF.", Toast.LENGTH_LONG).show()
        }
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
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Header
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Saved Report", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Water Intake Card
            item {
                WellnessDisplayCard(
                    icon = Icons.Default.WaterDrop,
                    iconColor = Color(0xFF4285F4),
                    title = "Water Intake",
                    value = water,
                    unit = "glasses (250ml each)"
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Sleep Timings Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NightlightRound, contentDescription = null, tint = Color(0xFF9C27B0))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Sleep Timings", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TimeDisplayBox(label = "Bedtime", time = bedTime, icon = Icons.Default.Bedtime, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(20.dp))
                            TimeDisplayBox(label = "Wake Up", time = wakeTime, icon = Icons.Default.WbSunny, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Results Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Wellness Score", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "$score%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Report generated for $username ($email). Focus on maintaining these healthy habits! 💪",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Saved! Banner
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0F0E8)),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "PDF saved as wellnesspdf_${username.replace(" ", "_")}.pdf", color = DarkGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun WellnessDisplayCard(icon: ImageVector, iconColor: Color, title: String, value: String, unit: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.width(100.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFF0F2F5))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = unit, color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun TimeDisplayBox(label: String, time: String, icon: ImageVector, modifier: Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFF0F2F5))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = time, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
            }
        }
    }
}
