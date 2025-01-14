package com.example.upialerts.data

data class PaymentNotification(
    val appName: String,
    val amount: Double,
    val timestamp: Long,
    val senderName: String,
    val message: String? = null,
    val type: TransactionType = TransactionType.RECEIVED // Default to RECEIVED as we only track donations
)

enum class TransactionType {
    RECEIVED // Removed SENT as we only track donations
} 