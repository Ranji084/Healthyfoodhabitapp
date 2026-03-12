package com.simats.Healthyfoodhabitapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.SessionManager
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

@Composable
fun WellnessScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    var waterGlasses by remember { mutableStateOf(sessionManager.getWaterIntake().toString()) }
    
    var bedHour by remember { mutableStateOf(22) }
    var bedMin by remember { mutableStateOf(45) }
    var wakeHour by remember { mutableStateOf(6) }
    var wakeMin by remember { mutableStateOf(0) }
    
    val quickWaterOptions = listOf("2", "4", "6", "8", "10")

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
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8F0FE),
                        modifier = Modifier.size(45.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = Color(0xFF4285F4), modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Wellness Check", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Water Intake Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF4285F4))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Water Intake", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.width(100.dp).height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFF0F2F5))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = waterGlasses, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "glasses (250ml each)", color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            quickWaterOptions.forEach { option ->
                                val isSelected = waterGlasses == option
                                Surface(
                                    modifier = Modifier.clickable { 
                                        waterGlasses = option
                                        sessionManager.saveWaterIntake(option.toInt())
                                    }.height(40.dp).width(60.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (isSelected) Color(0xFF4285F4) else Color(0xFFF0F2F5).copy(alpha = 0.5f)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = option, color = if (isSelected) Color.White else DarkGreen, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFF4285F4), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
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
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Bedtime, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Bedtime", fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    NumberScrollPicker(value = bedHour, range = 1..24, onValueChange = { bedHour = it }, modifier = Modifier.weight(1f))
                                    Text(" : ", fontWeight = FontWeight.Bold, color = DarkGreen)
                                    NumberScrollPicker(value = bedMin, range = 0..59, onValueChange = { bedMin = it }, modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.WbSunny, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Wake Up", fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    NumberScrollPicker(value = wakeHour, range = 1..24, onValueChange = { wakeHour = it }, modifier = Modifier.weight(1f))
                                    Text(" : ", fontWeight = FontWeight.Bold, color = DarkGreen)
                                    NumberScrollPicker(value = wakeMin, range = 0..59, onValueChange = { wakeMin = it }, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Action Button
            item {
                Button(
                    onClick = { 
                        // Calculate Sleep Duration
                        val sleepStart = bedHour + (bedMin / 60.0)
                        val sleepEnd = wakeHour + (wakeMin / 60.0)
                        var duration = sleepEnd - sleepStart
                        if (duration < 0) duration += 24
                        
                        navController.navigate("report?water=$waterGlasses&sleep=$duration&bedTime=$bedHour:$bedMin&wakeTime=$wakeHour:$wakeMin") 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Process & Get Results 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
