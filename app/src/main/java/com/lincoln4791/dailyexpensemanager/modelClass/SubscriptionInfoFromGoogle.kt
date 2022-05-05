package com.lincoln4791.dailyexpensemanager.modelClass

data class SubscriptionInfoFromGoogle(
    val acknowledged: Boolean,
    val autoRenewing: Boolean,
    val orderId: String,
    val packageName: String,
    val productId: String,
    val purchaseState: Int,
    val purchaseTime: Long,
    val purchaseToken: String,
    val quantity: Int
)