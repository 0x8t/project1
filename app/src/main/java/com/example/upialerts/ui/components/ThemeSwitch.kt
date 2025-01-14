package com.example.upialerts.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun ThemeSwitch(
    onThemeChange: (Boolean) -> Unit,
    showIcon: Boolean = false
) {
    var isDark by remember { mutableStateOf(false) }
    
    val iconTint by animateColorAsState(
        targetValue = if (isDark) Color.Yellow else Color.DarkGray,
        animationSpec = tween(300, easing = LinearEasing),
        label = "icon_tint"
    )

    if (showIcon) {
        Icon(
            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = "Theme Toggle",
            tint = iconTint
        )
    } else {
        IconButton(
            onClick = {
                isDark = !isDark
                onThemeChange(isDark)
            }
        ) {
            Icon(
                imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = "Theme Toggle",
                tint = iconTint
            )
        }
    }
} 