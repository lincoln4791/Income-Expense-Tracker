package com.lincoln4791.dailyexpensemanager.common

import android.database.sqlite.SQLiteOpenHelper
import com.lincoln4791.dailyexpensemanager.common.SQLiteHelper
import android.database.sqlite.SQLiteDatabase
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import android.content.ContentValues
import android.content.Context
import com.lincoln4791.dailyexpensemanager.common.NodeName
import android.content.Intent
import android.database.Cursor
import android.util.Log
import com.lincoln4791.dailyexpensemanager.view.Transactions
import com.lincoln4791.dailyexpensemanager.common.Extras

class SQLiteHelper(private val context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(QUERY_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun saveData(posts: MC_Posts): Long {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NodeName.POST_DESCRIPTION, posts.postDescription)
        contentValues.put(NodeName.POST_TYPE, posts.postType)
        contentValues.put(NodeName.POST_CATEGORY, posts.postCategory)
        contentValues.put(NodeName.POST_AMOUNT, posts.postAmount)
        contentValues.put(NodeName.POST_TIME, posts.postTime)
        contentValues.put(NodeName.POST_YEAR, posts.postYear)
        contentValues.put(NodeName.POST_MONTH, posts.postMonth)
        contentValues.put(NodeName.POST_DAY, posts.postDay)
        contentValues.put(NodeName.POST_DATE_TIME, posts.postDateTime)
        contentValues.put(NodeName.TIME_STAMP, posts.timeStamp)
        Log.d("tag", "amount " + posts.postAmount)
        return sqLiteDatabase.insert(MY_TABLE, null, contentValues)
    }

    fun updateData(ID: String, posts: MC_Posts): Boolean {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NodeName.POST_DESCRIPTION, posts.postDescription)
        contentValues.put(NodeName.POST_TYPE, posts.postType)
        contentValues.put(NodeName.POST_CATEGORY, posts.postCategory)
        contentValues.put(NodeName.POST_AMOUNT, posts.postAmount)
        contentValues.put(NodeName.POST_TIME, posts.postTime)
        contentValues.put(NodeName.POST_YEAR, posts.postYear)
        contentValues.put(NodeName.POST_MONTH, posts.postMonth)
        contentValues.put(NodeName.POST_DAY, posts.postDay)
        contentValues.put(NodeName.POST_DATE_TIME, posts.postDateTime)
        contentValues.put(NodeName.TIME_STAMP, posts.timeStamp)
        sqLiteDatabase.update(MY_TABLE, contentValues, "ID=?", arrayOf(ID))
        return true
    }

    fun loadAllTransactions(): Cursor {
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(QUERY_LOAD_ALL_TRANSACTIONS, null)
    }

    fun loadTypeWise(type: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postType='" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadCategoryWise(category: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postCategory= '" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadTypeAndCategoryWise(
        type: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postType= '" + type + "' AND postCategory= '" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearWise(year: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearTypeWise(year: String, type: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postType= '" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearCategoryWise(
        year: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postCategory= '" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearTypeCategoryWise(
        year: String,
        type: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postCategory= '" + category + "' AND postType= '" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthWise(year: String, month: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthTypeWise(year: String, month: String, type: String): Cursor {
        Log.d("tag", "in sql year $year")
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear = '" + year + "' AND postMonth = '" + month + "' AND postType ='" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthCategoryWise(year: String, month: String, category: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postCategory='" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthTypeCategoryWise(
        year: String,
        month: String,
        type: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postCategory='" + category + "' AND postType='" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthDayWise(year: String, month: String, day: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postDay='" + day + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthDayTypeWise(year: String, month: String, day: String, type: String): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postDay='" + day + "' AND postType= '" + type + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthDayCategoryWise(
        year: String,
        month: String,
        day: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postDay='" + day + "' AND postCategory= '" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun loadYearMonthDayTypeCategoryWise(
        year: String,
        month: String,
        day: String,
        type: String,
        category: String
    ): Cursor {
        val query =
            "SELECT * from " + MY_TABLE + " WHERE postYear= '" + year + "' AND postMonth= '" + month + "' AND postDay='" + day + "' AND postType= '" + type + "' AND postCategory= '" + category + "'"
        val sqLiteDatabase = this.readableDatabase
        return sqLiteDatabase.rawQuery(query, null)
    }

    fun deleteData(id: Int) {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(MY_TABLE, "ID = ? ", arrayOf(id.toString()))
        val intent = Intent(context, Transactions::class.java)
        intent.putExtra(Extras.TYPE, Constants.TYPE_ALL)
        context!!.startActivity(intent)
    }

    fun deleteDataAll() {
        //String query = "DELETE from "+MY_TABLE;
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(MY_TABLE, null, null)
        sqLiteDatabase.close()
    }

    companion object {
        private const val DATABASE_NAME = "MyDatabase"
        const val MY_TABLE = "MyTable"
        private const val QUERY_LOAD_ALL_TRANSACTIONS = "SELECT * from " + MY_TABLE + ""
        const val QUERY_CREATE_TABLE =
            "CREATE TABLE " + MY_TABLE + " (ID integer primary Key autoincrement, postDescription varchar, postCategory varchar," +
                    " postType varchar, postAmount varchar," +
                    "postTime varchar, postDay varchar, postMonth varchar, postYear varchar, dateTime varchar, timeStamp varchar) "
    }
}