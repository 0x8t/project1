package com.example.upialerts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.upialerts.data.AppPreferences
import com.example.upialerts.ui.theme.BicubikFont
import kotlinx.coroutines.launch
import com.example.upialerts.service.PaymentNotificationListener

@Composable
fun DrawerContent(
    onThemeChange: (Boolean) -> Unit,
    onDrawerClose: () -> Unit,
    isDarkTheme: Boolean,
    appPreferences: AppPreferences
) {
    var showTokenDialog by remember { mutableStateOf(false) }
    var showLogScreen by remember { mutableStateOf(false) }
    var currentToken by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Collect saved token
    LaunchedEffect(Unit) {
        appPreferences.apiToken.collect { token ->
            currentToken = token
        }
    }

    TokenDialog(
        showDialog = showTokenDialog,
        currentToken = currentToken,
        onDismiss = { showTokenDialog = false },
        onSave = { token ->
            currentToken = token
            scope.launch {
                appPreferences.saveApiToken(token)
                PaymentNotificationListener.updateToken(token)
            }
        }
    )

    if (showLogScreen) {
        LogScreen(onClose = { showLogScreen = false })
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "UPI ALERTS",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = BicubikFont
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

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
                    showTokenDialog = true
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

            // Add Log Button
            NavigationDrawerItem(
                icon = { 
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "View Logs"
                    )
                },
                label = { 
                    Text(
                        text = "View Logs",
                        fontFamily = FontFamily.SansSerif
                    )
                },
                selected = false,
                onClick = { 
                    showLogScreen = true
                    onDrawerClose()
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
} 