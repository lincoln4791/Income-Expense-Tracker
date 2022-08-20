package com.lincoln4791.dailyexpensemanager.di.modules

import android.content.Context
import com.lincoln4791.dailyexpensemanager.Repository
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


}