package com.example.upialerts

import android.content.Intent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.upialerts.data.PaymentNotification
import com.example.upialerts.service.PaymentNotificationListener
import com.example.upialerts.ui.components.DrawerContent
import com.example.upialerts.ui.components.NotificationCard
import com.example.upialerts.ui.theme.UPIAlertsTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.temporal.ChronoUnit
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings
import android.content.Context
import kotlinx.coroutines.delay
import com.example.upialerts.ui.theme.BicubikFont
import com.example.upialerts.data.AppPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.upialerts.api.AlertsApi
import com.example.upialerts.data.LogManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import android.util.Log
import kotlinx.coroutines.isActive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults

class MainActivity : ComponentActivity() {
    private lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences = AppPreferences(this)
        
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val systemInDarkTheme = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemInDarkTheme) }
            
            // Collect theme preference
            LaunchedEffect(Unit) {
                appPreferences.isDarkTheme.collect { savedTheme ->
                    darkTheme = savedTheme ?: systemInDarkTheme
                }
            }
            
            UPIAlertsTheme(darkTheme = darkTheme) {
                MainScreen(
                    isDarkTheme = darkTheme,
                    onThemeChange = { isDark ->
                        darkTheme = isDark
                        scope.launch {
                            appPreferences.saveThemePreference(isDark)
                        }
                    },
                    appPreferences = appPreferences
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    appPreferences: AppPreferences
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Collect notifications
    val notifications by PaymentNotificationListener.notifications.collectAsStateWithLifecycle()
    
    // Check if notification permission is granted
    val isPermissionGranted = remember {
        mutableStateOf(isNotificationServiceEnabled(context))
    }
    
    var isServiceEnabled by remember { mutableStateOf(isPermissionGranted.value) }
    var isValidating by remember { mutableStateOf(false) }

    // Request notification access only when permission is not granted
    LaunchedEffect(isServiceEnabled) {
        if (isServiceEnabled && !isPermissionGranted.value) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            context.startActivity(intent)
        }
    }

    // Update permission state when resuming
    LaunchedEffect(Unit) {
        isPermissionGranted.value = isNotificationServiceEnabled(context)
        isServiceEnabled = isPermissionGranted.value
    }

    // Collect token as state
    val token by appPreferences.apiToken.collectAsState(initial = "")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onThemeChange = onThemeChange,
                onDrawerClose = {
                    scope.launch { drawerState.close() }
                },
                isDarkTheme = isDarkTheme,
                appPreferences = appPreferences
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
                            text = "UPI ALERTS",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = BicubikFont,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isServiceEnabled,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = {
                            isValidating = true
                            scope.launch {
                                try {
                                    if (token.isBlank()) {
                                        snackbarHostState.showSnackbar(
                                            message = "Please set API token first",
                                            duration = SnackbarDuration.Short
                                        )
                                        return@launch
                                    }
                                    
                                    val api = AlertsApi.getInstance()
                                    val success = api.sendTestAlert(token)
                                    
                                    snackbarHostState.showSnackbar(
                                        message = if (success) "Test successful! Alert sent" else "Test failed",
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(
                                        message = "Test failed: ${e.message}",
                                        duration = SnackbarDuration.Long,
                                        withDismissAction = true
                                    )
                                } finally {
                                    isValidating = false
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(56.dp)
                        ) {
                            if (isValidating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Test API Connection"
                                )
                            }
                        }
                    }
                }
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
                            onCheckedChange = { newValue ->
                                if (newValue && !isPermissionGranted.value) {
                                    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                                    context.startActivity(intent)
                                }
                                isServiceEnabled = newValue
                            }
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

// Add this function to check if notification listener permission is granted
private fun isNotificationServiceEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat?.contains(packageName) == true
}