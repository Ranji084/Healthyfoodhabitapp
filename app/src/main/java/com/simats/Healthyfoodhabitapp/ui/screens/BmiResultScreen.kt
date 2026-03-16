package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiResultScreen(navController: NavController, bmi: String, category: String) {
    val bmiValue = bmi.toFloatOrNull() ?: 0f
    
    val (statusColor, description) = when {
        bmiValue < 18.5f -> Color(0xFF2196F3) to "You are in the Underweight range. It's important to eat a balanced diet and consult with a healthcare provider."
        bmiValue < 25f -> Color(0xFF4CAF50) to "You are in the Normal weight range. Great job maintaining a healthy lifestyle!"
        bmiValue < 30f -> Color(0xFFFFA000) to "You are in the Overweight range. Consider increasing physical activity and managing calorie intake."
        else -> Color(0xFFEF5350) to "You are in the Obese range. We recommend speaking with a doctor or nutritionist for personalized guidance."
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BMI Analysis", fontWeight = FontWeight.Bold, color = DarkGreen) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // BMI Circle
            Surface(
                shape = CircleShape,
                color = statusColor.copy(alpha = 0.1f),
                modifier = Modifier.size(200.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, statusColor)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Your BMI", fontSize = 16.sp, color = Color.Gray)
                    Text(text = bmi, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Category Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = statusColor)
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Description
            Text(
                text = description,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = DarkGreen.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Back to Results", fontWeight = FontWeight.Bold)
            }
        }
    }
}
