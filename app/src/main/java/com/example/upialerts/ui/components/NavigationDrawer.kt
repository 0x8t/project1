package com.example.upialerts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    onThemeChange: (Boolean) -> Unit,
    onDrawerClose: () -> Unit,
    isDarkTheme: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .padding(vertical = 12.dp)
        ) {
            // App Name in drawer header with matching top app bar height
            Box(
                modifier = Modifier.height(64.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Donation Tracker",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            // Source Code Option
            NavigationDrawerItem(
                icon = { 
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Token Management"
                    )
                },
                label = { 
                    Text(
                        text = "Add/Edit Token",
                        fontFamily = FontFamily.SansSerif
                    )
                },
                selected = false,
                onClick = { 
                    /* TODO: Implement token management */ 
                    onDrawerClose()
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // Theme Option
            NavigationDrawerItem(
                icon = { 
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Theme Toggle",
                        tint = if (isDarkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = { 
                    Text(
                        text = if (isDarkTheme) "Dark Theme" else "Light Theme",
                        fontFamily = FontFamily.SansSerif
                    )
                },
                selected = false,
                onClick = { onThemeChange(!isDarkTheme) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
} 