package com.lincoln4791.dailyexpensemanager.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.modelClass.Banner

@Database(entities = [MC_Posts::class,MC_Cards::class,MC_MonthlyReport::class, Banner::class], version = 2,exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun dbDao(): DatabaseDao
    companion object DatabaseBuilder {
        private var INSTANCEE : AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if(INSTANCEE==null){
                INSTANCEE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                ).addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCEE as AppDatabase
        }




        // Migrations
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Banner` (`created_date` TEXT NOT NULL, `deleted_date` TEXT NOT NULL, `is_active` TEXT NOT NULL, `link` TEXT NOT NULL, `media_url` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`name`))")

            }
        }

    }

}