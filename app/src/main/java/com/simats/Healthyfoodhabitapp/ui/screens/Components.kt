package com.simats.Healthyfoodhabitapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.Healthyfoodhabitapp.ui.theme.DarkGreen

data class MealTypeData(val name: String, val emoji: String)

@Composable
fun BottomNavBar(navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntry
    val currentRoute = currentBackStackEntry?.destination?.route
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp)),
        color = DarkGreen,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(navController, "home", Icons.Default.Home, currentRoute)
            BottomNavItem(navController, "wellness", Icons.Default.WaterDrop, currentRoute)
            Spacer(modifier = Modifier.width(40.dp))
            BottomNavItem(navController, "progress", Icons.Default.BarChart, currentRoute)
            BottomNavItem(navController, "settings", Icons.Default.Settings, currentRoute)
        }
    }
}

@Composable
fun BottomNavItem(navController: NavController, route: String, icon: ImageVector, currentRoute: String?) {
    Icon(
        imageVector = icon,
        contentDescription = route,
        tint = if (currentRoute == route) Color.White else Color.White.copy(alpha = 0.5f),
        modifier = Modifier
            .size(28.dp)
            .clickable { 
                if (currentRoute != route) {
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo("home") { saveState = true }
                    }
                } 
            }
    )
}

@Composable
fun SectionHeader(title: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGreen.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ResultAlertCard(title: String, description: String, bgColor: Color, iconColor: Color, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, color = DarkGreen.copy(alpha = 0.8f), fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
fun RecommendationItem(emoji: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
    }
}

@Composable
fun NumberScrollPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFF0F2F5)),
            color = Color.Transparent
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value.toString().padStart(2, '0'),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 250.dp)
        ) {
            range.forEach { i ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = i.toString().padStart(2, '0'),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        ) 
                    },
                    onClick = {
                        onValueChange(i)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    iconBgColor: Color,
    iconTintColor: Color = DarkGreen,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconBgColor,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTintColor, modifier = Modifier.size(26.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF8BC34A).copy(alpha = 0.6f))
        }
    }
}

@Composable
fun NutrientSmallCard(label: String, value: String, modifier: Modifier, valueColor: Color = DarkGreen) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F2F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(45.dp)) {
                CircularProgressIndicator(
                    progress = { 0.6f },
                    modifier = Modifier.fillMaxSize(),
                    color = if(valueColor == DarkGreen) Color(0xFF8BC34A) else valueColor,
                    strokeWidth = 3.dp,
                    trackColor = Color(0xFFF0F2F5),
                    strokeCap = StrokeCap.Round
                )
                Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

@Composable
fun SmallStatCard(modifier: Modifier, icon: String, value: String, label: String, bgColor: Color) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(text = icon, fontSize = 22.sp) }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkGreen)
            Text(text = label, fontSize = 13.sp, color = DarkGreen.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun ActionCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFFF1F1),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color(0xFFEF5350), modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun MealItem(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F2F5).copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(text = icon, fontSize = 22.sp) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
