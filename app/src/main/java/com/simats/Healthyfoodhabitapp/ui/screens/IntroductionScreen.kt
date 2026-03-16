package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun IntroductionScreen(navController: NavController) {
    var currentPage by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About App", fontWeight = FontWeight.Bold, color = DarkGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (currentPage == 1) "🥗" else "📈",
                    fontSize = 100.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = if (currentPage == 1) "Track Your Meals" else "Analyze Progress",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (currentPage == 1) 
                        "Log what you eat daily and get instant nutritional insights." 
                        else "Monitor your health journey with detailed charts and AI advice.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    if (currentPage == 1) {
                        currentPage = 2
                    } else {
                        currentPage = 1
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                containerColor = DarkGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = if (currentPage == 1) Icons.AutoMirrored.Filled.ArrowForward else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate"
                )
            }
        }
    }
}
