package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.billingclient.api.*
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.AdUnitIds
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.SubscriptionUtil
import com.lincoln4791.dailyexpensemanager.common.util.NetworkCheck
import com.lincoln4791.dailyexpensemanager.databinding.FragmentSubscriptionBinding
import com.lincoln4791.dailyexpensemanager.modelClass.SubscriptionInfoFromGoogle
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class Subscription : BaseFragment<FragmentSubscriptionBinding>(FragmentSubscriptionBinding::inflate), OnUserEarnedRewardListener {
    @Inject lateinit var prefManager: PrefManager
    private lateinit var navCon: NavController
    private lateinit var billingClient: BillingClient
    private lateinit var rewardedAd: RewardedAd
    private lateinit var adLoadingBar: Dialog
    private var isAlreadySubscribed = false

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                Log.d("Subscription", "Current Offers length -> ${purchases.size} ")
                for (purchase in purchases) {
                    val g = Gson()
                    g.fromJson(purchase.originalJson,
                        SubscriptionInfoFromGoogle::class.java)
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        prefManager.isPremiumUser = true
                        prefManager.isSubscriptionActiveInGooglePlayAccount = true

                        Toast.makeText(requireContext(),
                            "Subscription Purchase Successful",
                            Toast.LENGTH_LONG).show()

                        if (!purchase.isAcknowledged) {
                            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                            CoroutineScope(Dispatchers.IO).launch {
                                withContext(Dispatchers.IO) {
                                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                                }
                            }
                        }
                        goBack()
                    }
                }
            } else {
                Log.d("Subscription", "Purchase Failed, Error -> ${billingResult.responseCode} ")
                //Toast.makeText(this@Subscription,"Something Went Wrong In Purchase",Toast.LENGTH_SHORT).show()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag", "OnBackPressCalled -> Monthly")
                    navCon.navigateUp()
                    //goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun goBack() {
        val action = SubscriptionDirections.actionSubscriptionToHomeFragment()
        navCon.navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unloadProgressBar()
        navCon = Navigation.findNavController(view)

        init()
        initBillingClient(view)
        changeSubscriptionWiseUI()


        binding.cvSubscriptionMonthly.setOnClickListener {
            if (prefManager.subscriptionProductID == Constants.SUBSCRIPTION_MONTHLY) {
                SubscriptionUtil.goToSpecificSubscriptionInGooglePlay(requireContext(),
                    Constants.SUBSCRIPTION_MONTHLY)
            } else {
                initSubscriptionProcess(view, Constants.SUBSCRIPTION_MONTHLY)
            }
        }

        binding.cvSubscriptionYearly.setOnClickListener {
            if (prefManager.subscriptionProductID == Constants.SUBSCRIPTION_YEARLY) {
                SubscriptionUtil.goToSpecificSubscriptionInGooglePlay(requireContext(),
                    Constants.SUBSCRIPTION_YEARLY)
            } else {
                initSubscriptionProcess(view, Constants.SUBSCRIPTION_YEARLY)
            }
        }

        binding.cvWatchAd.setOnClickListener {
            adLoadingBar.show()
            loadRewardedAd()
        }

        binding.ivBack.setOnClickListener {
            goBack()
        }

    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun init() {
        adLoadingBar = Dialog(requireContext())
        val adLoadingBarView = layoutInflater.inflate(R.layout.content_ad_loading_bar, null, false)
        adLoadingBar.setContentView(adLoadingBarView)
        adLoadingBar.setCancelable(false)


        binding.tvAdDetails.text = "Watch one video ad\nto remove ad for ${prefManager.adRemoveDurationDayByAd} day"

    }

    private fun initBillingClient(view: View) {
        billingClient = BillingClient.newBuilder(requireContext().applicationContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        startBillingConnection(view) {

        }

    }

    private fun startBillingConnection(view: View, callback: (isConnected: Boolean) -> Unit) {
        //Toast.makeText(this@Subscription,"Init Subscription Called",Toast.LENGTH_SHORT).show()

        if (NetworkCheck.isConnect(requireContext())) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    Log.d("Subscription", "Billing Connected")
                    callback(true)
                    //querySubscriptions()
                }

                override fun onBillingServiceDisconnected() {
                    callback(false)
                    Log.d("Subscription", "Billing Disconnected, try again after a moment")
                    //Toast.makeText(this@Subscription,"Billing Service Disconnected",Toast.LENGTH_SHORT).show()
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        } else {
            callback(false)
            showNoInternetConnection(view)
        }


    }

    private fun showNoInternetConnection(view: View) {
        val rootLayout = view.findViewById<ConstraintLayout>(R.id.mainLayout)
        val snackBar = Snackbar
            .make(rootLayout, "No Internet", Snackbar.LENGTH_INDEFINITE)
            .setAction("ok") {

            }
        snackBar.show()

    }

    private fun showSubscriptionActiveDialog(view: View) {
        val rootLayout = view.findViewById<ConstraintLayout>(R.id.mainLayout)
        val snackBar = Snackbar
            .make(rootLayout, "Another Subscription already active", Snackbar.LENGTH_INDEFINITE)
            .setAction("ok") {

            }
        snackBar.show()

    }


    private fun querySubscriptions(callback: (isSuccess: Boolean, subscriptionInfo: SubscriptionInfoFromGoogle?) -> Unit) {

        try {
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { billingResult: BillingResult, purchases: MutableList<Purchase> ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    Log.d("Subscription", "Purchased Product Length -> ${purchases.size}")
                    if (purchases.size > 0) {
                        isAlreadySubscribed = true
                        for (purchase in purchases) {
                            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                //Acknowledge purchase
                                if (!purchase.isAcknowledged) {
                                    val acknowledgePurchaseParams =
                                        AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchase.purchaseToken)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.IO) {
                                            billingClient.acknowledgePurchase(
                                                acknowledgePurchaseParams.build())
                                        }
                                    }
                                }

                                val g = Gson()
                                val purchaseInfo = g.fromJson(purchase.originalJson,
                                    SubscriptionInfoFromGoogle::class.java)
                                prefManager.isPremiumUser = true
                                prefManager.isSubscriptionActiveInGooglePlayAccount = true
                                prefManager.subscriptionProductID = purchaseInfo.productId
                                Log.d("Subscription",
                                    "Purchased Products are -> ${purchase.purchaseToken}::: product id -> ${purchaseInfo.productId} ::: Account Identifiers: profile-> -> ${purchase.accountIdentifiers!!.obfuscatedProfileId}, account -> ${purchase.accountIdentifiers!!.obfuscatedAccountId}")
                                callback(true, purchaseInfo)
                            }
                        }
                    } else {
                        prefManager.isPremiumUser = false
                        prefManager.isSubscriptionActiveInGooglePlayAccount = false
                        prefManager.subscriptionProductID = ""
                        Log.d("Subscription", "Purchase size is zero")
                        callback(true, null)
                    }

                } else {
                    prefManager.isPremiumUser = false
                    prefManager.isSubscriptionActiveInGooglePlayAccount = false
                    prefManager.subscriptionProductID = ""
                    callback(true, null)
                    Log.d("Subscription", "Purchase size maybe null, else part ${purchases.size}")
                }
                Log.d("Subscription", "End of QueryPurchaseAsync Callback")

            }
        } catch (e: Exception) {
            callback(false, null)
            e.printStackTrace()
        }

    }

    private fun initSubscriptionProcess(view: View, pID: String) {

        startBillingConnection(view) { isSuccess ->
            if (isSuccess) {
                querySubscriptions { isSuccessTwo: Boolean, subscriptionInfoFromGoogle: SubscriptionInfoFromGoogle? ->
                    Log.d("Subscription",
                        "success -> $isSuccessTwo ::: info -> $subscriptionInfoFromGoogle")
                    if (isSuccessTwo) {
                        Log.d("Subscription", "Query Purchase returned")
                        if (subscriptionInfoFromGoogle == null) {
                            Log.d("Subscription", "Query Purchase returned true")
                            doSubscription(pID)
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                /*Toast.makeText(requireContext(),"Another Subscription Already active",Toast.LENGTH_SHORT).show()*/
                                showSubscriptionActiveDialog(view)
                            }
                            Log.d("Subscription",
                                "Another Subscription Already Active -> ${subscriptionInfoFromGoogle.productId}")
                        }
                    } else {
                        Log.d("Subscription", "Query Purchase Returned Error")
                    }
                }
            } else {
                Log.d("Subscription", "Billing Not Connected")
            }
        }

    }

    private fun doSubscription(pID: String) {
        val skuList: ArrayList<String> = arrayListOf()
        skuList.add(pID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        // leverage querySkuDetails Kotlin extension function
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, mutableList ->
            Log.d("Subscription",
                "billing result -? ${billingResult.responseCode} :::${billingResult.debugMessage} ")
            if (mutableList != null) {
                for (skuDetails in mutableList) {
                    if (skuDetails.sku == pID) {
                        //Now update the UI
                        launchPurchaseFlow(skuDetails, pID)
                    }
                }
            } else {
                //launchPurchaseFlow(,pID)
                Log.d("Subscription", "purchase list is null")
            }
        }
    }


    private fun launchPurchaseFlow(skuDetails: SkuDetails, pID: String) {
        Log.d("Subscription", "SKU Details -> $skuDetails")
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
    }

    private fun changeSubscriptionWiseUI() {
        if (prefManager.isPremiumUser) {
            if (prefManager.subscriptionProductID == Constants.SUBSCRIPTION_MONTHLY) {
                binding.tvSubscriptionMonthly.visibility = View.INVISIBLE
                binding.ivSubscriptionMonthly.visibility = View.VISIBLE

                binding.tvSubscriptionYearly.visibility = View.VISIBLE
                binding.ivSubscriptionYearly.visibility = View.INVISIBLE
                binding.cvSubscriptionMonthly.isClickable = false
                binding.cvSubscriptionYearly.isClickable = true
                Log.d("Subscription", "Monthly Active")
            } else if (prefManager.subscriptionProductID == Constants.SUBSCRIPTION_YEARLY) {
                binding.tvSubscriptionMonthly.visibility = View.VISIBLE
                binding.ivSubscriptionMonthly.visibility = View.INVISIBLE

                binding.tvSubscriptionYearly.visibility = View.INVISIBLE
                binding.ivSubscriptionYearly.visibility = View.VISIBLE
                binding.cvSubscriptionMonthly.isClickable = true
                binding.cvSubscriptionYearly.isClickable = false
                Log.d("Subscription", "Yearly Active")
            }


        } else {
            binding.tvSubscriptionMonthly.visibility = View.VISIBLE
            binding.ivSubscriptionMonthly.visibility = View.INVISIBLE

            binding.tvSubscriptionYearly.visibility = View.VISIBLE
            binding.ivSubscriptionYearly.visibility = View.INVISIBLE
            binding.cvSubscriptionMonthly.isClickable = true
            binding.cvSubscriptionYearly.isClickable = true
        }
    }


    private fun loadRewardedAd() {
        Log.d("RewardAd", "load Reward Ad Called")
        // Use the test ad unit ID to load an ad.
        RewardedAd.load(requireContext(), AdUnitIds.REWARDED_AD_REMOVE,
            AdRequest.Builder().setHttpTimeoutMillis(Constants.REWARD_AD_TIMEOUT).build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("RewardAd", "onAdLoaded -> ${ad.responseInfo}")

                    rewardedAd.fullScreenContentCallback = object :
                        FullScreenContentCallback() {
                        /** Called when the ad failed to show full screen content.  */
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d("RewardAd", "onAdFailedToShowFullScreenContent")
                        }

                        /** Called when ad showed the full screen content.  */
                        override fun onAdShowedFullScreenContent() {
                            Log.d("RewardAd", "onAdShowedFullScreenContent")
                        }

                        /** Called when full screen content is dismissed.  */
                        override fun onAdDismissedFullScreenContent() {
                            Log.d("RewardAd", "onAdDismissedFullScreenContent")

                        }
                    }
                    try {
                        adLoadingBar.dismiss()
                        showRewardedAD()
                        Log.d("RewardAd", "onAdDismissedFullScreenContent")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    try {
                        Log.e("RewardAd",
                            "onAdFailedToLoad -> ${loadAdError.message} :: code-> ${loadAdError.code}:: cause ${loadAdError.cause} :: response ${loadAdError.responseInfo}")
                        adLoadingBar.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            })
    }

    fun showRewardedAD() {
        rewardedAd.show(requireActivity(), this@Subscription)
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        Log.d("Subscription", "Reward Earned-> ${p0.amount}")
        //86400000 = one day in mill

        val timeToBeAdded = (prefManager.adRemoveDurationDayByAd.toLong()) * (Constants.INTERVAL_DAILY.toLong())
        val newExpireTime = System.currentTimeMillis() + timeToBeAdded

        prefManager.adRemoveExpireTime = newExpireTime
        Log.d("rewardAd","Reward Granted, new expire time is -> $newExpireTime")
        prefManager.isAdRemoved = true


    }

    private fun loadProgressBar() {
        binding.mainLoadingBar.visibility = View.VISIBLE
        binding.clContainer.visibility=View.GONE
    }

    private fun unloadProgressBar(){
        binding.mainLoadingBar.visibility = View.GONE
        binding.clContainer.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        try {
            rewardedAd.fullScreenContentCallback = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onDestroy()
    }

}