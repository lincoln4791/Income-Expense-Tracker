package com.lincoln4791.dailyexpensemanager.di.modules

import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyAppModule {

    @Provides
    fun provideRepository(@ApplicationContext context: Context)  = Repository(provideDao(context))

    @Provides
    fun provideDatabase(@ApplicationContext context: Context)  = AppDatabase.getInstance(context)

    @Provides
    fun provideDao(@ApplicationContext context: Context)  = provideDatabase(context).dbDao()

    @Provides
    fun provideSampleDateFormat()  = SimpleDateFormat("yyyy-MM-dd")

    @Provides
    fun providePrefManager(@ApplicationContext context: Context)  = PrefManager(context)

    @Provides
    fun provideFirebaseAnalytics()  = Firebase.analytics

    fun provideFirebaseAuth() = Firebase.auth


}