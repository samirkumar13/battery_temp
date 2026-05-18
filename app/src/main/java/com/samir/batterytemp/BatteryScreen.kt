package com.samir.batterytemp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BatteryScreen(
    temperature: Float,
    status: String,
    history: List<String>
) {
    val tempColor = when {
        temperature >= 45f -> Color(0xFFFF4444)
        temperature >= 35f -> Color(0xFFFF9800)
        else -> Color(0xFF4CAF50)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0D0D), Color(0xFF1A1A2E))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
            text = "Battery Monitor",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFAAAAAA),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Temperature circle card
        Card(
            modifier = Modifier.size(200.dp),
            shape = RoundedCornerShape(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (temperature == 0f) "--" else "$temperature",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = tempColor
                    )
                    Text(
                        text = "°C",
                        fontSize = 22.sp,
                        color = tempColor.copy(alpha = 0.8f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Status badge
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        ) {
            Text(
                text = "⚡ Status: $status",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }

        // Temp warning
        if (temperature >= 45f) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF4444).copy(alpha = 0.2f))
            ) {
                Text(
                    text = "⚠️ High Temperature Warning!",
                    fontSize = 14.sp,
                    color = Color(0xFFFF4444),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // History section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(last 20 readings)",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (history.isEmpty()) {
            Text(
                text = "Waiting for readings...",
                color = Color(0xFF555555),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(history) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Text(
                            text = entry,
                            fontSize = 13.sp,
                            color = Color(0xFFCCCCCC),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
