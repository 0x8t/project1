package com.example.upialerts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upialerts.data.PaymentNotification
import com.example.upialerts.ui.components.NotificationCard
import com.example.upialerts.ui.components.ThemeSwitch
import com.example.upialerts.ui.components.DrawerContent
import com.example.upialerts.ui.theme.UPIAlertsTheme
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            
            UPIAlertsTheme(darkTheme = darkTheme) {
                MainScreen(
                    isDarkTheme = darkTheme,
                    onThemeChange = { darkTheme = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val notifications = remember {
        mutableStateListOf(
            PaymentNotification(
                appName = "Google Pay",
                amount = 500.0,
                timestamp = Instant.now().minus(5, ChronoUnit.MINUTES).toEpochMilli(),
                senderName = "John Doe",
                message = "Thank you for your work!"
            ),
            PaymentNotification(
                appName = "PhonePe",
                amount = 1000.0,
                timestamp = Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli(),
                senderName = "Jane Smith",
                message = "Keep up the great work"
            )
        )
    }

    var isServiceEnabled by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onThemeChange = onThemeChange,
                onDrawerClose = {
                    scope.launch { drawerState.close() }
                },
                isDarkTheme = isDarkTheme
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.32f) // Makes the scrim more opaque
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Donation Tracker",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Service Switch
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notification Service",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                        Switch(
                            checked = isServiceEnabled,
                            onCheckedChange = { isServiceEnabled = it }
                        )
                    }
                }

                // Notifications List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(notification = notification)
                    }
                }
            }
        }
    }
}