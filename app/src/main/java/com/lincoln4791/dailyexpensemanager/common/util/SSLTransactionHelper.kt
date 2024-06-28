package com.lincoln4791.dailyexpensemanager.common.util

import android.content.Context

import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCProductInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType

object SSLTransactionHelper {


    fun getSSLCommerzInitializer(
        amount: Double,
        transactionId: String,
        productCategory: String
    ): SSLCommerzInitialization {
        return SSLCommerzInitialization(
            "linco615047b39a651",
            "linco615047b39a651@ssl",
            amount,
            SSLCCurrencyType.BDT,
            transactionId,
            productCategory,
            SSLCSdkType.TESTBOX
        )
    }

    fun getProductInitializer(): SSLCProductInitializer {
        return SSLCProductInitializer(
            "test_name", "test details",
            SSLCProductInitializer.ProductProfile.General("test_product_id_123","")
        )
    }

    fun getCustomerInitializer(context: Context): SSLCCustomerInfoInitializer {
        val prefManager = PrefManager(context)
        return SSLCCustomerInfoInitializer(
            "test_name", "test@mail.com",
            "", "", "", "", "01909600890"
        )
    }


    fun getAdditionalInitializer(context: Context,productID:String,productName: String) : SSLCAdditionalInitializer {
        val prefManager = PrefManager(context)
        val additionalInitializer = SSLCAdditionalInitializer()
        additionalInitializer.valueA = "2"
        additionalInitializer.valueB = productID
        additionalInitializer.valueC = productName
        return additionalInitializer
    }





/*
    suspend fun initPendingSSLSubscriptions (context: Context, repository: Repository){
        val prefManager = PrefManager(context)
         prefManager.lastPendingSubscriptionCheckTime

        if(System.currentTimeMillis()-prefManager.lastPendingSubscriptionCheckTime > 30000){
            try {
                val pendingSubscriptions = repository.getAllSubscriptionSSLStatusModel()

                for(element in pendingSubscriptions){
                    if(!element.isValidated!!){
                        val cTime= CurrentDate.getTimeInMIllFromOnline(context)
                        //val cTime= System.currentTimeMillis()
                        if(cTime!=null){
                            if(cTime- element.purchaseTime!! > Constants.INTERVAL_1_DAY){
                                Log.d("ssl","pending subscriptiin will delete-> Product not claimed")
                                deletePendingSSLSubscriptionByTransactionID(repository,
                                    element.transactionID
                                )
                            }
                            else{
                                Log.d("ssl","pending subscriptiin trying to validate")

                                if(element.validation_id.isNullOrEmpty()){
                                    Log.d("ssl","trying to validate by transactionID")
                                    val response = validateFromPaServerByTransactionID(element.userID!!,element.xToken!!,element.transactionID, element.productId!!,
                                        element.productName!!)
                                    handleValidationResponse(repository,element.transactionID,response)
                                }
                                else{
                                    Log.d("ssl","trying to validate by validationid")
                                    val response = validateFromPaServerByValidationID(element.userID!!,element.xToken!!,element.validation_id!!, element.productId!!,
                                        element.productName!!)
                                    handleValidationResponse(repository,element.transactionID,response)
                                }
                            }
                        }
                        //validateFromPaServer(element.userID!!,element.xToken!!,element.validation_id, element.productId!!)
                    }
                    else{
                        Log.d("ssl","pending subscriptiin will delete")
                        deletePendingSSLSubscriptionByTransactionID(repository,element.transactionID)
                    }
                }

            }
            catch (e:Exception){
                e.printStackTrace()
            }
            prefManager.lastPendingSubscriptionCheckTime=System.currentTimeMillis()
        }
        else{
            Log.d("ssl","Pending subscription check later")
        }
    }
    */

/*
    private suspend fun handleValidationResponse(repository: Repository,transactionID: String,response: Response<SSLSaveSubscriptionResponseModel>) {
        if(response.isSuccessful){
            if(response.code()==200){
                if(response.body()!=null){
                    if(response.body()!!.success=="true"){
                        repository.updateSubscriptionSSLStatusModelAsValidated(transactionID)
                    }
                    else{
                        Log.e("ssl","pending subscription validate retry failed -> success = false :: message -> ${response.errorBody()}")
                    }
                }
                else{
                    Log.e("ssl","pending subscription validate retry failed -> body = null")
                }
            }
            else{
                Log.e("ssl","pending subscription validate retry failed -> code -> ${response.code()}")
            }
        }
        else{
            Log.e("ssl","pending subscription validate retry failed by retrofit")
        }
    }

    */

/*    suspend fun validateFromPaServerByValidationID(
        userid: String,
        xToken: String,
        valID: String,
        productID: String,
        productName:String): Response<SSLSaveSubscriptionResponseModel> {

        return RetrofitClientStaging.instance.saveSSLSubscriptionByValidationID(
            xToken,
            valID,
            userid,
            productID,
            productName
       )
    }*/

/*    suspend fun validateFromPaServerByTransactionID(
        userid: String,
        xToken: String,
        tran_id: String,
        productID: String,
        productName:String): Response<SSLSaveSubscriptionResponseModel> {

        return RetrofitClientStaging.instance.saveSSLSubscriptionByTransactionID(
            xToken,
            tran_id,
            userid,
            productID,
            productName
        )
    }*/

/*    suspend fun deletePendingSSLSubscriptionByTransactionID(repository: Repository, transactionID:String){
        repository.deleteSaveSubscriptionSSLStatusByTransactionID(transactionID)
    }*/





}