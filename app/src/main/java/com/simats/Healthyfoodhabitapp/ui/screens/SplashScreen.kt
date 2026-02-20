package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen
import com.simats.Healthyfoodhabitapp.ui.theme.Cream

@Composable
fun SplashScreen(onProceedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGreen)
            .padding(24.dp)
    ) {
        // Floating Food Items (Using Emojis as placeholders for the user's assets)
        Text(text = "🥦", fontSize = 48.sp, modifier = Modifier.align(Alignment.TopStart).padding(top = 40.dp, start = 20.dp))
        Text(text = "🥕", fontSize = 40.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(bottom = 150.dp, end = 30.dp))
        Text(text = "🍎", fontSize = 40.sp, modifier = Modifier.align(Alignment.CenterStart).padding(top = 150.dp, start = 10.dp))
        Text(text = "🥑", fontSize = 48.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 120.dp, end = 40.dp))

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Salad Bowl Placeholder
            Surface(
                modifier = Modifier.size(160.dp),
                shape = RoundedCornerShape(80.dp),
                color = Color.Transparent
            ) {
                Text(
                    text = "🥗",
                    fontSize = 100.sp,
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Healthy Food Habit",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Eat well, live better 🌿",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp
            )
        }

        // Proceed Button
        Button(
            onClick = onProceedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Cream),
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Proceed",
                    color = DarkGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = DarkGreen
                )
            }
        }
    }
}
