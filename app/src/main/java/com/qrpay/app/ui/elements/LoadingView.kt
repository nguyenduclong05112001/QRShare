package com.qrpay.app.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(withBackground: Boolean = true) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(if (withBackground) Color(0xFF1B1B1B) else Color.Transparent),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(6.dp))
                .padding(16.dp),
            color = Color(0xFFB71564)
        )
    }
}