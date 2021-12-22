package com.lincoln4791.dailyexpensemanager.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lincoln4791.dailyexpensemanager.model.MC_Posts

@Database(entities = [MC_Posts::class], version = 1,exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun dbDao(): DatabaseDao
    companion object DatabaseBuilder {
        private var INSTANCEE : AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if(INSTANCEE==null){
                INSTANCEE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "MyDatabaseRoom"
                ).build()
            }
            return INSTANCEE as AppDatabase
        }
    }
}