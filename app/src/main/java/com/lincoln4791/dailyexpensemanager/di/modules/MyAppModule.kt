package com.lincoln4791.dailyexpensemanager.di.modules

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat

@Module
@InstallIn(SingletonComponent::class)
object MyAppModule {

    @Provides
    fun provideRepository(@ApplicationContext context: Context)  = Repository(provideDao(context))

    @Provides
    fun provideDatabase(@ApplicationContext context: Context)  = AppDatabase.getInstance(context)

    @Provides
    fun provideDao(@ApplicationContext context: Context)  = provideDatabase(context).dbDao()

    @SuppressLint("SimpleDateFormat")
    @Provides
    fun provideSampleDateFormat()  = SimpleDateFormat("yyyy-MM-dd")

    @Provides
    fun providePrefManager(@ApplicationContext context: Context)  = PrefManager(context)

    @Provides
    fun provideFirebaseAnalytics()  = Firebase.analytics

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseReference() : DatabaseReference = Firebase.database.reference

    @Provides
    fun provideLinearLayoutManager(@ApplicationContext context : Context) : LinearLayoutManager {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        return linearLayoutManager
    }
/*
    @Provides
    @ViewModelScoped
    @Named(DaggerConsts.YEAR)
    fun provideYear() {
        val simpleYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    }*/
}