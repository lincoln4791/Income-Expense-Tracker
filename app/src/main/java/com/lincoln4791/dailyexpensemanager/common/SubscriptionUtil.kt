package com.lincoln4791.dailyexpensemanager.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.billingclient.api.*
import com.google.gson.Gson
import com.lincoln4791.dailyexpensemanager.modelClass.SubscriptionInfoFromGoogle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriptionUtil {
    companion object {

        private lateinit var billingClient: BillingClient

        private val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                // To be implemented in a later section.
            }


        private fun establishConnection(context: Context) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        checkSubscription(context)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    //establishConnection(context)
                }
            })
        }


        private fun checkSubscription(context: Context) {
            val prefManager = PrefManager(context)
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { billingResult: BillingResult, purchases: MutableList<Purchase> ->
                Log.d("Subscription",
                    "Billing Result -> ${billingResult.responseCode} ::: Purchased Product Length- -> ${purchases.size}")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    Log.d("Subscription", "Purchased Product Length -> ${purchases.size}")
                    if (purchases.size > 0) {
                        prefManager.isSubscriptionActiveInGooglePlayAccount = true
                        for (purchase in purchases) {
                            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                val g = Gson()
                                val purchaseInfo = g.fromJson(purchase.originalJson,
                                    SubscriptionInfoFromGoogle::class.java)
                                Log.d("Subscription",
                                    "Purchased Products are -> ${purchase.purchaseToken}::: product id -> ${purchaseInfo.productId}")
                                prefManager.isSubscriptionActiveInGooglePlayAccount=true
                                prefManager.isPremiumUser = true
                                prefManager.subscriptionProductID  = purchaseInfo.productId

                                if (!purchase.isAcknowledged) {
                                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.purchaseToken)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val ackPurchaseResult = withContext(Dispatchers.IO) {
                                            billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                                        }
                                    }
                                }

                            }
                        }
                    }
                    else{
                        prefManager.isSubscriptionActiveInGooglePlayAccount=false
                        prefManager.isPremiumUser  = false
                        prefManager.subscriptionProductID  = ""
                    }
                } else {
                    /*prefManager.isSubscriptionActiveInGooglePlayAccount = false
                    prefManager.isPremiumUser = false
                    prefManager.subscriptionProductID  = ""*/
                    Log.d("Subscription", "Purchase size is 0 or less -> ${purchases.size}")

                }
            }

        }


         fun initBillingClient(context: Context) {
            billingClient = BillingClient.newBuilder(context.applicationContext)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build()

            establishConnection(context.applicationContext)

        }


        fun goToSpecificSubscriptionInGooglePlay(context: Context, pID: String) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions?sku=$pID&package=com.lincoln4791.dailyexpensemanager"))
            context.startActivity(intent)
        }

        fun goToAllSubscriptionsInGooglePlay(context: Context) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions"))
            context.startActivity(intent)
        }


    }

}